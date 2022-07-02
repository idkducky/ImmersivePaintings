package immersive_paintings.resources;

import immersive_paintings.Main;
import immersive_paintings.util.ImageManipulations;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class Painting {
    public final int width;
    public final int height;
    public final int resolution;

    public final String name;
    public final String author;
    public boolean datapack;

    public Texture texture;
    public Texture half;
    public Texture thumbnail;

    public Painting(@Nullable NativeImage image, int width, int height, int resolution) {
        this(image, width, height, resolution, "", "", false, UUID.randomUUID().toString());
    }

    public Painting(@Nullable NativeImage image, int width, int height, int resolution, String name, String author, boolean datapack, String hash) {
        this.texture = new Texture(image, hash);
        this.half = new Texture(null, hash + "_half");
        this.thumbnail = new Texture(null, hash + "_thumbnail");

        this.width = width;
        this.height = height;
        this.resolution = resolution;
        this.name = name;
        this.author = author;
        this.datapack = datapack;
    }

    public Painting(NativeImage image, int resolution) {
        this(image, image.getWidth() / resolution, image.getHeight() / resolution, resolution, "", "", false, UUID.randomUUID().toString());
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("width", width);
        nbt.putInt("height", height);
        nbt.putInt("resolution", resolution);
        nbt.putString("name", name);
        nbt.putString("author", author);
        nbt.putBoolean("datapack", datapack);
        nbt.putString("hash", texture.hash);
        return nbt;
    }

    public NbtCompound toFullNbt() {
        NbtCompound nbt = toNbt();
        nbt.putIntArray("texture", ImageManipulations.imageToInts(texture.image));
        return nbt;
    }

    public static Painting fromNbt(NbtCompound nbt) {
        int width = nbt.getInt("width");
        int height = nbt.getInt("height");
        int resolution = nbt.getInt("resolution");
        String name = nbt.getString("name");
        String author = nbt.getString("author");
        boolean datapack = nbt.getBoolean("datapack");
        String hash = nbt.getString("hash");

        NativeImage image = null;
        if (nbt.contains("texture")) {
            image = ImageManipulations.intsToImage(width * resolution, height * resolution, nbt.getIntArray("texture"));
        }

        return new Painting(image, width, height, resolution, name, author, datapack, hash);
    }

    public int getPixelWidth() {
        return width * resolution;
    }

    public int getPixelHeight() {
        return height * resolution;
    }

    public Texture getTexture(Type type) {
        return switch (type) {
            case FULL -> texture;
            case HALF -> half;
            case THUMBNAIL -> thumbnail;
        };
    }

    public enum Type {
        FULL,
        HALF,
        THUMBNAIL
    }

    public class Texture {
        public NativeImage image;
        public boolean requested = false;
        public Identifier textureIdentifier = Main.locate("textures/block/frame/canvas.png");
        public Resource resource;
        public final String hash;

        public Texture(NativeImage image, String hash) {
            this.image = image;
            this.hash = hash;
        }
    }
}