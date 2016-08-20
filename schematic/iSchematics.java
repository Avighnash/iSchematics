package us.universalpvp.schematic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import us.universalpvp.schematic.loader.SavedBlock;
import us.universalpvp.schematic.loader.Selection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avigh on 8/19/2016.
 */
public class iSchematics extends JavaPlugin {

    @Override
    public void onEnable() {

    }

    public static List<Block> placeStructure(String fileName, Location baseLocation) {

        Selection selection;

        List<Block> allBlocks = new ArrayList<>();
        List<SavedBlock> attachables = new ArrayList<>();
        List<SavedBlock> doors = new ArrayList<>();
        List<Block> portals = new ArrayList<>();

        try {
            selection = new Selection(baseLocation.getWorld(), readFromFile("plugins/structures/" + fileName + ".json"));


            for (SavedBlock savedBlock : selection.getSavedBlocks()) {

                Block realBlock = baseLocation.clone().add(savedBlock.getRelativeX(), savedBlock.getRelativeY(), savedBlock.getRelativeZ()).getBlock();

                allBlocks.add(realBlock);


                if (savedBlock.getType() == Material.PORTAL) {
                    portals.add(realBlock);
                } else if (savedBlock.isAttachable()) {
                    attachables.add(0, savedBlock);
                } else if (savedBlock.getType().toString().contains("DOOR")) {
                    doors.add(savedBlock);
                } else {
                    realBlock.setType(savedBlock.getType());
                    realBlock.setData(savedBlock.getData());
                }

            }


            for (SavedBlock door : doors) {
                Block realBlock = baseLocation.clone().add(door.getRelativeX(), door.getRelativeY(), door.getRelativeZ()).getBlock();

                realBlock.setType(door.getType());
                realBlock.setData(door.getData());

                Block above = realBlock.getRelative(BlockFace.UP);

                above.setType(door.getType());
                above.setData((byte) (door.getData() + 8));

            }


            for (SavedBlock savedBlock : attachables) {

                Block realBlock = baseLocation.clone().add(savedBlock.getRelativeX(), savedBlock.getRelativeY(), savedBlock.getRelativeZ()).getBlock();
                Block below = realBlock.getRelative(BlockFace.DOWN);
                Material belowType = below.getType();

                byte belowData = below.getData();

                if (!below.getType().isSolid()) {
                    below.setType(Material.STONE);
                }

                realBlock.setType(savedBlock.getType());
                realBlock.setData(savedBlock.getData());

                below.setType(belowType);
                below.setData(belowData);

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return allBlocks;

    }

    public static void writeToFile(String filename, Selection selection) throws IOException {
        File structures = new File("plugins/structures");

        if (!(structures.exists())) {
            structures.mkdirs();
        }

        FileWriter writer = new FileWriter("plugins/structures/" + filename + ".json");
        writer.write(selection.toJSON().toJSONString());
        writer.close();
    }

    public static JSONObject readFromFile(String path) throws FileNotFoundException, IOException, ParseException {
        return (JSONObject) new JSONParser().parse(new FileReader(path));
    }


}
