package net.easyhopper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EasyHopperConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
        .getConfigDir().resolve("easyredstone.json");

    public static boolean easyRedstoneEnabled = true;
    public static boolean showActionBarMessage = true;
    public static List<String> enabledItems = defaultItems();

    private static List<String> defaultItems() {
        List<String> list = new ArrayList<>();
        list.add("minecraft:hopper");
        list.add("minecraft:redstone");
        list.add("minecraft:redstone_torch");
        list.add("minecraft:repeater");
        list.add("minecraft:comparator");
        list.add("minecraft:observer");
        list.add("minecraft:dropper");
        list.add("minecraft:dispenser");
        list.add("minecraft:piston");
        list.add("minecraft:sticky_piston");
        list.add("minecraft:lever");
        list.add("minecraft:stone_button");
        list.add("minecraft:oak_button");
        list.add("minecraft:rail");
        list.add("minecraft:powered_rail");
        list.add("minecraft:detector_rail");
        list.add("minecraft:activator_rail");
        list.add("minecraft:redstone_block");
        list.add("minecraft:target");
        list.add("minecraft:daylight_detector");
        list.add("minecraft:tripwire_hook");
        list.add("minecraft:note_block");
        return list;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            Data data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                easyRedstoneEnabled = data.easyRedstoneEnabled;
                showActionBarMessage = data.showActionBarMessage;
                if (data.enabledItems != null && !data.enabledItems.isEmpty()) {
                    enabledItems = data.enabledItems;
                }
            }
        } catch (Exception e) {
            EasyHopperMod.LOGGER.warn("Failed to load config, using defaults", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Data data = new Data();
            data.easyRedstoneEnabled = easyRedstoneEnabled;
            data.showActionBarMessage = showActionBarMessage;
            data.enabledItems = enabledItems;
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (Exception e) {
            EasyHopperMod.LOGGER.error("Failed to save config", e);
        }
    }

    public static boolean isAllowedItem(Item item) {
        Identifier id = Registries.ITEM.getId(item);
        return enabledItems.contains(id.toString());
    }

    private static class Data {
        boolean easyRedstoneEnabled = true;
        boolean showActionBarMessage = true;
        List<String> enabledItems;
    }
                  }
