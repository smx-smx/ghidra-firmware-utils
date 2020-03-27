/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package firmware.cbfs;

import java.io.File;
import java.io.IOException;

import ghidra.app.util.bin.ByteProvider;
import ghidra.formats.gfilesystem.FSRL;
import ghidra.formats.gfilesystem.FSRLRoot;
import ghidra.formats.gfilesystem.FileSystemService;
import ghidra.formats.gfilesystem.factory.GFileSystemFactoryFull;
import ghidra.formats.gfilesystem.factory.GFileSystemProbeBytesOnly;
import ghidra.util.exception.CancelledException;
import ghidra.util.task.TaskMonitor;
import utilities.util.ArrayUtilities;

public class CBFSFileSystemFactory implements GFileSystemFactoryFull<CBFSFileSystem>, GFileSystemProbeBytesOnly {
	@Override
	public CBFSFileSystem create(FSRL containerFSRL, FSRLRoot targetFSRL, ByteProvider byteProvider, File containerFile,
			FileSystemService fsService, TaskMonitor monitor) throws IOException, CancelledException {
		CBFSFileSystem fs = new CBFSFileSystem(targetFSRL);
		try {
			fs.mount(byteProvider, monitor);
			return fs;
		} catch (IOException ioe) {
			fs.close();
			throw ioe;
		}
	}

	@Override
	public int getBytesRequired() {
		return CBFSConstants.CBFS_FILE_SIGNATURE.length;
	}

	@Override
	public boolean probeStartBytes(FSRL containerFSRL, byte[] startBytes) {
		return ArrayUtilities.arrayRangesEquals(CBFSConstants.CBFS_FILE_SIGNATURE, 0, startBytes, 0,
				CBFSConstants.CBFS_FILE_SIGNATURE.length);
	}

}
