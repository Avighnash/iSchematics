package us.universalpvp.schematic.nms;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avigh on 8/20/2016.
 */
public class CuboidSelection {

    private Location leftClickLocation;
    private Location rightClickLocation;
    private List<Block> savedBlocks;

    public CuboidSelection(Location leftClickLocation, Location rightClickLocation) {
        this.leftClickLocation = leftClickLocation;
        this.rightClickLocation = rightClickLocation;

        this.savedBlocks = new ArrayList<>();
    }

    public Location getLeftClickLocation() {
        return leftClickLocation;
    }

    public Location getRightClickLocation() {
        return rightClickLocation;
    }

    public void saveCurrentSelection() {
        List<Block> blocks = new ArrayList<>();

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

                        blocks.add(b);
                    }
                }
            }
        }
        this.savedBlocks = blocks;
    }

    public List<Block> getSavedBlocks() {
        return savedBlocks;
    }
}
