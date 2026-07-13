package me.moof.spawnrules.config;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import java.util.List;

public class GuiItemBuilder {
    public static ItemStack create(Item item, String name, List<Component> loreLines) {
        ItemStack stack = new ItemStack(item);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        if (loreLines != null && !loreLines.isEmpty()) {
            stack.set(DataComponents.LORE, new ItemLore(loreLines));
        }
        return stack;
    }
}
