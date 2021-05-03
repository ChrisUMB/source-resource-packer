package blue.sparse.srp;

import blue.sparse.vfi.assets.ValveTexture;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class RSPPacker {

	private RSPPacker() {
	}

//	public static void pack() throws IOException {
//		File packs = SRPFiles.RESOURCE_PACKS;
//		File[] files = packs.listFiles();
//
//		if (files == null || files.length <= 0) {
//			System.out.println("No resource packs found.");
//			return;
//		}
//
//		packDefaultResources();
//
//		for (File packFolder : files) {
//			pack(packFolder);
//		}
//	}

	public static void packDefaultResources(ResourceProgress progress) throws IOException {
		System.out.println("Packing default resources...");
		File dir = RSPFiles.WORKING_DIR;
		File defaultResources = RSPFiles.DEFAULT_RESOURCES;
		List<String> assetExtensions = new ArrayList<>(RSPFiles.ASSET_EXTENSIONS_LIST);

		List<Path> paths = Files.walk(defaultResources.toPath()).filter(path -> {
			File file = path.toFile();
			String fullPath = file.getAbsolutePath();
			String[] split = fullPath.split("\\.");

			if (file.isDirectory()) {
				return false;
			}

			if (split.length <= 0) {
				return false;
			}

			String extension = split[split.length - 1];

			if (extension.equalsIgnoreCase("png")) {
				return false;
			}

			if (!assetExtensions.contains(extension)) {
				System.out.println("Unpackable file: " + fullPath);
				return false;
			}

			return true;
		}).collect(Collectors.toList());

		int total = paths.size();
		int complete = 0;

		for (Path path : paths) {
			File file = path.toFile();
			String fullPath = file.getAbsolutePath();
			String[] split = fullPath.split("\\.");

			String extension = split[split.length - 1];

			String asset = split[0].substring(defaultResources.getAbsolutePath().length() + 1);
			File out = new File(dir, asset + "." + extension);

			try {
				progress.apply(asset, (double) complete / (double) total);
				Files.copy(file.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}

			complete++;
		}
	}

	//TODO: Account for typos. If the file they're trying to overwrite doesn't exist in the cached resources,
	//it more than likely isn't a valid path and won't do anything to the game resources properly since they
	//named something wrong.
	public static void pack(File pack, ResourceProgress progress) throws IOException {

		File dir = RSPFiles.WORKING_DIR;

		if (!pack.isDirectory() && !pack.getName().endsWith(".zip")) {
			return;
		}

		System.out.println("Packing \"" + pack.getName() + "\"...");

		String name = pack.getName();
		List<Path> paths;

		boolean isZip = false;

		if (pack.isDirectory()) {
			paths = Files
					.walk(pack.toPath()).filter(path -> !path.toFile().isDirectory())
					.collect(Collectors.toList());
		} else {
			if (!name.endsWith(".zip")) {
				return;
			}

			isZip = true;

			FileSystem fs = FileSystems.newFileSystem(pack.toPath(), Collections.emptyMap());
			paths = StreamSupport.stream(fs.getRootDirectories().spliterator(), false).flatMap(root -> {
				try {
					return Files.walk(root);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null;
			}).filter(it -> !Files.isDirectory(it)).collect(Collectors.toList());
		}

		int total = paths.size();
		int count = 0;

		for (Path path : paths) {
			InputStream input = Files.newInputStream(path);
			String filePath = path.toAbsolutePath().toString();
			String[] split = filePath.split("\\.");
			String assetName;
			if (!isZip) {
				assetName = split[0].substring(pack.getAbsolutePath().length() + 1);
			} else {
				assetName = split[0];
			}

			String extension = split[1];

			progress.apply(assetName, (double) count / (double) total);

			if (extension.equalsIgnoreCase("png")) {
				try {
					BufferedImage image = ImageIO.read(input);
					ValveTexture texture = ValveTexture.create(image);
					texture.setReflectivity(new Vector3f(1f));
					File out = new File(dir, assetName + ".vtf");

					if (out.exists() && RSPConfig.MAKE_BACKUPS) {
						File copy = new File(out.getAbsolutePath() + ".bak");
						if (!copy.exists()) {
							Files.copy(out.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
							out.delete();
						}
					}

					texture.save(out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				File out = new File(dir, assetName + "." + extension);
				try {
					Files.copy(path, out.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			count++;
		}
	}
}
