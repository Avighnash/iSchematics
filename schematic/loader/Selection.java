package us.universalpvp.schematic.loader;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by avigh on 8/19/2016.
 */
public class Selection {

    private Location leftClickLocation;
    private Location rightClickLocation;
    private List<SavedBlock> savedBlocks;
    private Location center;

    public Selection() {
        this.savedBlocks = new ArrayList<>();
    }


    public Selection(World world, JSONObject object) {
        JSONArray blocks = (JSONArray) object.get("blocks");

        Iterator<JSONObject> iterator = blocks.iterator();

        while (iterator.hasNext()) {
            this.savedBlocks.add(new SavedBlock(iterator.next()));
        }

        this.center = new Location(world, (long) object.get("center-x"), (long) object.get("center-y"), (long) object.get("center-z"));
    }

    public void saveCurrentSelection() {
        List<SavedBlock> blocks = new ArrayList<>();

        if (leftClickLocation != null && rightClickLocation != null) {

            int xMin = Math.min(leftClickLocation.getBlockX(), rightClickLocation.getBlockX());
            int xMax = Math.max(leftClickLocation.getBlockX(), rightClickLocation.getBlockX());
            int yMin = Math.min(leftClickLocation.getBlockY(), rightClickLocation.getBlockY());
            int yMax = Math.max(leftClickLocation.getBlockY(), rightClickLocation.getBlockY());
            int zMin = Math.min(leftClickLocation.getBlockZ(), rightClickLocation.getBlockZ());
            int zMax = Math.max(leftClickLocation.getBlockZ(), rightClickLocation.getBlockZ());

            World world = leftClickLocation.getWorld();

            for (int x = xMin; x <= xMax; x++) {
                for (int y = yMin; y <= yMax; y++) {
                    for (int z = zMin; z <= zMax; z++) {
                        Block b = new Location(world, x, y, z).getBlock();
                        blocks.add(new SavedBlock(center, b));
                    }
                }
            }
        }
        this.savedBlocks = blocks;
    }

    public void setLeftClickLocation(Location leftClickLocation) {
        this.leftClickLocation = leftClickLocation;
    }

    public void setRightClickLocation(Location rightClickLocation) {
        this.rightClickLocation = rightClickLocation;
    }

    public List<SavedBlock> getSavedBlocks() {
        return savedBlocks;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();

        JSONArray blockDataArray = new JSONArray();

        for (SavedBlock block : this.savedBlocks)
            blockDataArray.add(block.toJSON());

        object.put("blocks", blockDataArray);
        object.put("center-x", this.center.getBlockX());
        object.put("center-y", this.center.getBlockY());
        object.put("center-z", this.center.getBlockZ());

        return object;
    }
}
