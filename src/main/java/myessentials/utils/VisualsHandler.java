package myessentials.utils;

import myessentials.MyEssentialsCore;
import myessentials.entities.api.Position;
import myessentials.entities.api.Volume;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VisualsHandler {

    public static final VisualsHandler instance = new VisualsHandler();

    private final List<VisualObject> markedObjects = new ArrayList<VisualObject>();

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent ev) {
        if (ev.side != Side.SERVER || ev.phase != TickEvent.Phase.START)
            return;


        for(Iterator<VisualObject> visualObjectIterator = markedObjects.iterator(); visualObjectIterator.hasNext(); ) {
            VisualObject visualObject = visualObjectIterator.next();

            if (!visualObject.packetSent) {
                for (Position pos : visualObject.positions) {
                    SPacketBlockChange packet = new SPacketBlockChange(WorldUtils.getServer().worldServerForDimension(pos.dim()), pos.toBlockPos());
                    packet.blockState = visualObject.block.getDefaultState();
                    visualObject.sendPacketToPlayer(packet);
                }
            }

            if (visualObject.deleted) {
                for (Position pos : visualObject.positions) {
                    SPacketBlockChange packet = new SPacketBlockChange(WorldUtils.getServer().worldServerForDimension(pos.dim()), pos.toBlockPos());
                    packet.blockState = WorldUtils.getServer().worldServerForDimension(pos.dim()).getBlockState(pos.toBlockPos());
                    visualObject.sendPacketToPlayer(packet);
                }
            }

            if (visualObject.deleted || visualObject.positions.isEmpty()) {
                visualObjectIterator.remove();
            }
        }
    }

    public void mark(Position pos, EntityPlayerMP caller, Block block, Object key) {
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.player == caller && visualObject.object.equals(key)) {
                visualObject.positions.add(pos);
                return;
            }
        }
        markedObjects.add(new VisualObject(caller, key, Arrays.asList(pos), block));
    }

    public void mark(List<Position> positions, EntityPlayerMP caller, Block block, Object key) {
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.player == caller && visualObject.object.equals(key)) {
                visualObject.positions.addAll(positions);
                return;
            }
        }
        markedObjects.add(new VisualObject(caller, key, positions, block));
    }

    public void mark(Position pos1, Position pos2, EntityPlayerMP caller, Block block, Object key) {

        List<Position> positions = new ArrayList<Position>();

        positions.add(pos1);
        positions.add(pos2);

        // On the X axis
        boolean xComparison = pos1.x() > pos2.x();
        positions.add(pos1.offset(xComparison ? -1 : 1, 0, 0));
        positions.add(pos1.offset(xComparison ? -2 : 2, 0, 0));
        positions.add(pos2.offset(xComparison ? -1 : 1, 0, 0));
        positions.add(pos2.offset(xComparison ? -2 : 2, 0, 0));

        // On the Z axis
        boolean zComparison = pos1.z() > pos2.z();
        positions.add(pos1.offset(0, 0, zComparison ? -1 : 1));
        positions.add(pos1.offset(0, 0, zComparison ? -2 : 2));
        positions.add(pos2.offset(0, 0, zComparison ? -1 : 1));
        positions.add(pos2.offset(0, 0, zComparison ? -2 : 2));

        if (pos1.y() != pos2.y()) {
            // On the Y axis
            boolean yComparison = pos1.y() > pos2.y();
            positions.add(pos1.offset(0, yComparison ? -1 : 1, 0));
            positions.add(pos1.offset(0, yComparison ? -2 : 2, 0));
            positions.add(pos2.offset(0, yComparison ? -1 : 1, 0));
            positions.add(pos2.offset(0, yComparison ? -2 : 2, 0));
        }
        mark(positions, caller, block, key);
    }

    public void mark(Volume v, EntityPlayerMP caller, Block block, Object key) {
        List<Position> positions = new ArrayList<Position>();
        Position min1 = v.getStartPos();
        Position min2 = min1.offset(v.getLengthX() - 1, 0, 0);
        Position min3 = min1.offset(0, 0, v.getLengthZ() - 1);
        Position min4 = min1.offset(v.getLengthX() - 1, 0, v.getLengthZ() - 1);
        Position max1 = min1.offset(0, v.getLengthY() - 1, 0);
        Position max2 = min1.offset(v.getLengthX() - 1, v.getLengthY() - 1, 0);
        Position max3 = min1.offset(0, v.getLengthY() - 1, v.getLengthZ() - 1);
        Position max4 = v.getEndPos();

        // Add all the corners
        positions.add(min1);
        positions.add(min2);
        positions.add(min3);
        positions.add(min4);
        positions.add(max1);
        positions.add(max2);
        positions.add(max3);
        positions.add(max4);

        // Add all the lines
        for (int i = 1; i < v.getLengthX() - 1; i++) {
            positions.add(min1.offset(i, 0, 0));
            positions.add(min3.offset(i, 0, 0));
            positions.add(max1.offset(i, 0, 0));
            positions.add(max3.offset(i, 0, 0));
        }
        for (int i = 1; i < v.getLengthY() - 1; i++) {
            positions.add(min1.offset(0, i, 0));
            positions.add(min2.offset(0, i, 0));
            positions.add(min3.offset(0, i, 0));
            positions.add(min4.offset(0, i, 0));
        }
        for (int i = 1; i < v.getLengthZ() - 1; i++) {
            positions.add(min1.offset(0, 0, i));
            positions.add(min2.offset(0, 0, i));
            positions.add(max1.offset(0, 0, i));
            positions.add(max2.offset(0, 0, i));
        }
        addMarkedBlocks(positions, caller, key, block);
    }


    public void markBorders(List<Volume> volumes, EntityPlayerMP player, Block block, Object key) {
        int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] dz = {0, 1, 1, 1, 0, -1, -1, -1};

        int x, y, z;

        List<Position> positions = new ArrayList<Position>();

        for (Volume v : volumes) {
            // Showing lines in borders
            for (int i = 0; i < 8; i += 2) {
                if (!volumes.contains(v.offset(dx[i] * 16, 0, dz[i] * 16))) {
                    if (dx[i] == 0) {
                        z = dz[i] == -1 ? v.minZ : v.maxZ;
                        x = v.minX;
                        for (int k = x + 1; k <= x + 14; k++) {
                            y = WorldUtils.getMaxHeightWithSolid(v.dim, k, z);
                            positions.add(new Position(k, y, z, v.dim));
                        }
                    } else {
                        x = dx[i] == -1 ? v.minX : v.maxX;
                        z = v.minZ;
                        for (int k = z + 1; k <= z + 14; k++) {
                            y = WorldUtils.getMaxHeightWithSolid(v.dim, x, k);
                            positions.add(new Position(x, y, k, v.dim));
                        }
                    }
                }
            }

            // Showing corners in borders
            for (int i = 1; i < 8; i += 2) {
                x = dx[i] == 1 ? v.minX : v.maxX;
                z = dz[i] == 1 ? v.minZ : v.maxZ;
                y = WorldUtils.getMaxHeightWithSolid(v.dim, x, z);
                positions.add(new Position(x, y, z, v.dim));
            }
        }

        addMarkedBlocks(positions, player, key, block);
    }

    /**
     * This method is gonna wait until the tick function clears the spot.
     */
    public void addMarkedBlocks(final List<Position> positions, final EntityPlayerMP player, final Object key, final Block block) {
        // Waits 5 milliseconds if there are still blocks to be deleted.
        Thread t = new Thread() {
            @Override
            public void run() {
                VisualObject visualObject = null;
                for(VisualObject marked : markedObjects) {
                    if(marked.player == player && marked.object == key) {
                        if (marked.deleted) {
                            while (markedObjects.contains(marked)) {
                                try {
                                    Thread.sleep(5);
                                } catch (Exception ex) {
                                    MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
                                }
                            }
                            break;
                        }
                    }
                }
                visualObject = new VisualObject(player, key, positions, block);
                markedObjects.add(visualObject);
            }
        };
        t.start();
    }

    /*
    public void updatePlotBorders(Plot plot) {
        List<EntityPlayerMP> callers = new ArrayList<EntityPlayerMP>();
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.isPlot() && visualObject.object.equals(plot)) {
                for(BlockCoords coords : visualObject.blockCoords) {
                    coords.deleted = true;
                }
                callers.add(visualObject.player);
            }
        }
        for(EntityPlayerMP player : callers) {
            markPlotBorders(plot, player);
        }
    }

    public void updateTownBorders(TownBlock.Container townBlocksContainer) {
        List<EntityPlayerMP> callers = new ArrayList<EntityPlayerMP>();
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.isTown() && visualObject.object.equals(townBlocksContainer)) {
                for(BlockCoords coords : visualObject.blockCoords) {
                    coords.deleted = true;
                }
                callers.add(visualObject.player);
            }
        }
        for(EntityPlayerMP player : callers) {
            markBorders(townBlocksContainer, player);
        }
    }
    */

    /**
     * Unmarks all the blocks that are linked to the player and object key.
     */
    public synchronized void unmark(EntityPlayerMP caller, Object key) {
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.player == caller && visualObject.object == key) {
                visualObject.deleted = true;
            }
        }

    }

    public synchronized void unmark(Object key) {
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.object == key) {
                visualObject.deleted = true;
            }
        }
    }

    public synchronized void unmark(EntityPlayerMP caller, Class<?> keyType) {
        for(VisualObject v : markedObjects) {
            if (v.player.equals(caller) && keyType.isInstance(v.object)) {
                v.deleted = true;
            }
        }
    }

    /*
    public void unmarkTowns(EntityPlayerMP caller) {
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.player == caller && visualObject.isTown()) {
                visualObject.deleted = true;
            }
        }
    }

    public void unmarkPlots(EntityPlayerMP caller) {
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.player == caller && visualObject.isPlot()) {
                visualObject.deleted = true;
            }
        }
    }
    */

    public boolean isBlockMarked(Position pos, EntityPlayerMP player) {
        for(VisualObject visualObject : markedObjects) {
            if(visualObject.player == player && !visualObject.deleted) {
                for (Position marked : visualObject.positions) {
                    if (marked.equals(pos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private class VisualObject {
        private EntityPlayerMP player;
        private Object object;
        private List<Position> positions;
        private boolean packetSent = false, deleted = false;
        private Block block;

        public VisualObject(EntityPlayerMP player, Object object, List<Position> positions, Block block) {
            this.player = player;
            this.object = object;
            this.positions = positions;
            this.block = block;
        }

        public void sendPacketToPlayer(Packet packet)
        {
            try {
                this.player.connection.sendPacket(packet);
            } catch(Exception e) {
                // Player not connected? Never mind then.
            }
        }
    }
}