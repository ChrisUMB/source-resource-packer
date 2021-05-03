package blue.sparse.srp;

import blue.sparse.vfi.assets.ValveTexture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.application.Application.launch;

public final class SRP {

	//TODO:
	/*
	Theoretically, resource packs could also include stuff like .sav and .cfg files.
	For cfg, I'd have to do a mix operation so it's not a pure override of the
	pack users entire settings, for instance. Pack creators would then only put what
	they intend to override about their CFG's in there, so people could make "cfg packs",
	or "save packs".
	 */

	public static void main(String[] args) throws IOException {
//		if (!SRPFiles.DATA.exists()) {
			launch(SRPApp.class, args);
//		} else {
//			launch(SRPApp.class, args);
//		}

//		STPFiles.makeDirectories();
//
//		File[] files = STPFiles.DEFAULT_RESOURCES.listFiles();
//
//		if (files == null || files.length <= 0) {
//			System.out.println("Extracting default resources to \"stp/resources\"...");
//			extractDefaultResources();
//		}
//
//		STPPacker.pack();
	}

	public static void extractDefaultResources(ResourceProgress progress) {
		File dir = SRPFiles.WORKING_DIR;
		String dirPath = dir.getAbsolutePath();

		try {
			List<String> extensions = SRPFiles.ASSET_EXTENSIONS_LIST;

			List<Path> assets = Files.walk(dir.toPath()).filter(it -> {
				String fullPath = it.toString();

				if (fullPath.startsWith(SRPFiles.DATA.getAbsolutePath())) {
					return false;
				}

				String[] split = fullPath.split("\\.");

				if (split.length <= 0) {
					return false;
				}

				String extension = split[split.length - 1];
				return extensions.contains(extension);

			}).collect(Collectors.toList());

			int totalFiles = assets.size();
			int processCount = 0;

			for (Path asset : assets) {
				File file = asset.toFile();
				String fullPath = asset.toString();

				String[] split = fullPath.split("\\.");
				String extension = split[split.length - 1];
				String assetName = fullPath.substring(dirPath.length() + 1);

				progress.apply(assetName, (double) processCount / (double) totalFiles);

				File out = new File(SRPFiles.DEFAULT_RESOURCES, assetName);
				out.getParentFile().mkdirs();
				try {
					if (extension.equalsIgnoreCase("vtf")) {
						try {
							ValveTexture texture = ValveTexture.load(file);
							File pngOut = new File(SRPFiles.DEFAULT_RESOURCES, assetName.replace(".vtf", ".png"));
							texture.export("PNG", pngOut);
						} catch (Throwable e) {
							System.out.println("Failed to export " + assetName);
						}
					}
					Files.copy(file.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}

				processCount++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
