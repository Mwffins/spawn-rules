package me.moof.spawnrules.config;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGuiMenu extends ChestMenu {
    private final SimpleContainer container;
    private final Map<Integer, EntityType<?>> slotToMob = new HashMap<>();
    private EntityType<?> activeMobConfig = null;
    private int currentPage = 0;

    public ConfigGuiMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(54));
    }

    private ConfigGuiMenu(int containerId, Inventory inventory, SimpleContainer container) {
        super(MenuType.GENERIC_9x6, containerId, inventory, container, 6);
        this.container = container;
        populateItems(inventory.player);
    }

    private void populateItems(Player player) {
        if (activeMobConfig == null) {
            populateMainScreen(player);
        } else {
            populateSubScreen(player);
        }
    }

    private void populateMainScreen(Player player) {
        slotToMob.clear();
        List<EntityType<?>> mobs = EntityFilter.getSpawnableMobs();
        int totalMobs = mobs.size();
        int maxPages = (int) Math.ceil((double) totalMobs / 45.0);
        
        if (currentPage >= maxPages) {
            currentPage = maxPages - 1;
        }
        if (currentPage < 0) {
            currentPage = 0;
        }

        for (int i = 0; i < 54; i++) {
            this.container.setItem(i, GuiItemBuilder.create(Items.STAINED_GLASS_PANE.gray(), " ", null));
        }

        int startIndex = currentPage * 45;
        for (int i = 0; i < 45; i++) {
            int mobIndex = startIndex + i;
            if (mobIndex >= totalMobs) {
                break;
            }
            EntityType<?> type = mobs.get(mobIndex);
            setMobMainListSlot(i, type);
        }

        if (currentPage > 0) {
            setControlSlot(45, "Previous Page", Items.ARROW, "Go to page " + currentPage);
        }

        List<Component> pageLore = new ArrayList<>();
        pageLore.add(Component.literal("Page " + (currentPage + 1) + " of " + maxPages).withStyle(ChatFormatting.GRAY));
        this.container.setItem(49, GuiItemBuilder.create(Items.BOOK, "Page Indicator", pageLore));

        if (currentPage < maxPages - 1) {
            setControlSlot(53, "Next Page", Items.ARROW, "Go to page " + (currentPage + 2));
        }

        setControlSlot(51, "Close Menu", Items.BARRIER, "Closes settings");
    }

    private void setMobMainListSlot(int slot, EntityType<?> type) {
        Item eggItem = SpawnEggItem.byId(type).map(Holder::value).orElse(Items.BARRIER);
        String name = type.getDescription().getString();

        List<Component> lore = new ArrayList<>();
        lore.add(Component.literal("Configure spawning dimensions").withStyle(ChatFormatting.GRAY));
        lore.add(Component.literal(""));
        
        lore.add(Component.literal("Overworld: ").withStyle(ChatFormatting.GRAY)
                .append(getDimensionStatusComponent("minecraft:overworld", type)));
        lore.add(Component.literal("Nether: ").withStyle(ChatFormatting.GRAY)
                .append(getDimensionStatusComponent("minecraft:the_nether", type)));
        lore.add(Component.literal("The End: ").withStyle(ChatFormatting.GRAY)
                .append(getDimensionStatusComponent("minecraft:the_end", type)));

        lore.add(Component.literal(""));
        lore.add(Component.literal("Click to configure").withStyle(ChatFormatting.YELLOW));

        this.container.setItem(slot, GuiItemBuilder.create(eggItem, name, lore));
        slotToMob.put(slot, type);
    }

    private Component getDimensionStatusComponent(String dimension, EntityType<?> type) {
        boolean blocked = ConfigManager.isBlocked(type, dimension);
        if (blocked) {
            return Component.literal("BLOCKED").withStyle(ChatFormatting.RED, ChatFormatting.BOLD);
        } else {
            return Component.literal("ALLOWED").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD);
        }
    }

    private void populateSubScreen(Player player) {
        for (int i = 0; i < 54; i++) {
            this.container.setItem(i, GuiItemBuilder.create(Items.STAINED_GLASS_PANE.gray(), " ", null));
        }

        Item eggItem = SpawnEggItem.byId(activeMobConfig).map(Holder::value).orElse(Items.BARRIER);
        String name = activeMobConfig.getDescription().getString();
        List<Component> activeLore = new ArrayList<>();
        activeLore.add(Component.literal("Configuring spawn rules for this mob").withStyle(ChatFormatting.GRAY));
        this.container.setItem(13, GuiItemBuilder.create(eggItem, name, activeLore));

        setSubScreenDimensionSlot(20, "minecraft:overworld", "Overworld", Items.GRASS_BLOCK);
        setSubScreenDimensionSlot(22, "minecraft:the_nether", "Nether", Items.NETHERRACK);
        setSubScreenDimensionSlot(24, "minecraft:the_end", "The End", Items.END_STONE);

        setControlSlot(40, "Back to Mob List", Items.ARROW, "Return to list of mobs");
    }

    private void setSubScreenDimensionSlot(int slot, String dimension, String displayName, Item item) {
        boolean mobBlocked = ConfigManager.isMobBlocked(activeMobConfig, dimension);
        boolean categoryBlocked = ConfigManager.isCategoryBlocked(activeMobConfig.getCategory().getName(), dimension);
        boolean blocked = mobBlocked || categoryBlocked;

        List<Component> lore = new ArrayList<>();
        if (blocked) {
            if (categoryBlocked && !mobBlocked) {
                lore.add(Component.literal("Status: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("BLOCKED (BY CATEGORY)").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)));
            } else {
                lore.add(Component.literal("Status: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("BLOCKED").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)));
            }
        } else {
            lore.add(Component.literal("Status: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("ALLOWED").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)));
        }
        lore.add(Component.literal(""));
        lore.add(Component.literal("Click to toggle individual block status").withStyle(ChatFormatting.YELLOW));

        this.container.setItem(slot, GuiItemBuilder.create(item, displayName, lore));
    }

    private void setControlSlot(int slot, String displayName, Item item, String description) {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.literal(description).withStyle(ChatFormatting.GRAY));
        this.container.setItem(slot, GuiItemBuilder.create(item, displayName, lore));
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        if (slotIndex >= 0 && slotIndex < 54) {
            handleSlotClick(slotIndex, player);
            populateItems(player);
            this.broadcastChanges();
        }
    }

    private void handleSlotClick(int slotIndex, Player player) {
        if (activeMobConfig == null) {
            if (slotIndex == 45) {
                if (currentPage > 0) {
                    currentPage--;
                }
                return;
            }
            if (slotIndex == 53) {
                List<EntityType<?>> mobs = EntityFilter.getSpawnableMobs();
                int totalMobs = mobs.size();
                int maxPages = (int) Math.ceil((double) totalMobs / 45.0);
                if (currentPage < maxPages - 1) {
                    currentPage++;
                }
                return;
            }
            if (slotIndex == 51) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.closeContainer();
                }
                return;
            }

            EntityType<?> type = slotToMob.get(slotIndex);
            if (type != null) {
                activeMobConfig = type;
            }
        } else {
            if (slotIndex == 20) {
                ConfigManager.toggleMob(activeMobConfig, "minecraft:overworld");
                return;
            }
            if (slotIndex == 22) {
                ConfigManager.toggleMob(activeMobConfig, "minecraft:the_nether");
                return;
            }
            if (slotIndex == 24) {
                ConfigManager.toggleMob(activeMobConfig, "minecraft:the_end");
                return;
            }
            if (slotIndex == 40) {
                activeMobConfig = null;
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }
}
