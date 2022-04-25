import { readdir } from "fs/promises";
import fs, { read } from "fs";
import path from "path";


export function isDir(filePath: string) {
  return fs.statSync(filePath).isDirectory();
}

export function directoryExist(fullPath: string) {
  return fs.existsSync(fullPath);
}

export async function tryFindItemInDir(directory: string, itemName: string) {
  const itemsInDir = await getDirStruct(directory);
  const requestedItem = itemsInDir.find((f) => path.parse(f).name == itemName);

  return { requestedItem, itemsInDir};
}

export async function getObjectInfo(dir: string,itemName:string) {
  const {requestedItem, itemsInDir} = await tryFindItemInDir(dir,itemName)
  const fullPath = path.join(dir,requestedItem ?? '') 

  return {fullPath, requestedItem, itemsInDir}
}

export async function getDirStruct(dirPath:string) {
  return await readdir(dirPath);
}
