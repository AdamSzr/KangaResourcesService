import { DiskObject } from "../models/DiskObject";
import { DirectoryInfo } from "../models/DirectoryInfo";
import fs from "fs";
import path from "path";

function produceDiskObject(dir: string, item: string) {
  let z = {} as DiskObject;
  const stats = fs.statSync(path.join(dir, item));
  z.type = stats.isDirectory() ? "DIR" : "FILE";
  z.size = z.type == "DIR" ? 0 : stats.size;
  z.name = item;
  z.createdAt = stats.ctime
  return z;
}

export function directoryAnalizer(baseDir: string, innerObjects: string[]) {
  let z = {} as DirectoryInfo;
  z.path = baseDir;
  z.items = innerObjects.map((i) => produceDiskObject(baseDir, i));
  return z;
}
