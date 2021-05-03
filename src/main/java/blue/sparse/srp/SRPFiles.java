package blue.sparse.srp;

import java.io.File;
import java.util.List;

public final class SRPFiles {

	private SRPFiles() {
	}

	public static final File WORKING_DIR = new File(System.getProperty("user.dir"));
	public static final File DATA = new File("srp");
	public static final File DEFAULT_RESOURCES = new File(DATA, "resources");
	public static final File RESOURCE_PACKS = new File("resourcepacks");

	public static final String[] ASSET_EXTENSIONS = new String[]{
			//TODO: Support more file types!
			"vtf", "vmt"//, "mdl", "vvd", "vtx", "phy", "pcf"
	};

	public static final List<String> ASSET_EXTENSIONS_LIST = List.of(ASSET_EXTENSIONS);

	public static void makeDirectories() {
		if (!DATA.exists()) {
			DATA.mkdirs();
		}

		if (!DEFAULT_RESOURCES.exists()) {
			DEFAULT_RESOURCES.mkdirs();
		}

		if (!RESOURCE_PACKS.exists()) {
			RESOURCE_PACKS.mkdirs();
		}
	}

}
