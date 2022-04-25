import { DiskObject } from "../models/DiskObject";
import { DirectoryInfo } from "../models/DirectoryInfo";
import fs from "fs";
import path from "path";

function produceDriveObject(dir: string, item: string) {
  let z = {} as DiskObject;
  const stats = fs.statSync(path.join(dir, item));
  z.type = stats.isDirectory() ? "DIR" : "FILE";
  z.size = z.type == "DIR" ? 0 : stats.size;
  z.name = item;
  z.createdAt = stats.ctime
  return z;
}

export function catalogueAnalizer(baseDir: string, innerObjects: string[]) {
  let z = {} as DirectoryInfo;
  z.path = baseDir;
  z.items = innerObjects.map((i) => produceDriveObject(baseDir, i));
  return z;
}
