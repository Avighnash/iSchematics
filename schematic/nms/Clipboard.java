package us.universalpvp.schematic.nms;


import org.bukkit.entity.Player;

/**
 * Created by avigh on 8/20/2016.
 */
public class Clipboard {

    private final Player holder;
    private final CuboidSelection selection;

    public Clipboard(Player holder, CuboidSelection selection) {
        this.selection = selection;
        this.holder = holder;
    }


    public CuboidSelection getSelection() {
        return selection;
    }

    public Player getHolder() {
        return holder;
    }
}
