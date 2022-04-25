import { readdir } from "fs/promises";
import fs, { read } from "fs";
import path from "path";
import { RequestError } from "../models/RequestError";
import { DirectoryInfo } from "../models/DirectoryInfo";


function isDir(filePath: string) {
  return path.parse(filePath as string).ext == "";
}

class ObjectInfo {
  public filePath;
  public baseDir;
  public isDir;
  public items;
  constructor(
    filePath: string,
    baseDir: string,
    isDir: boolean,
    innerItems: string[]
  ) {
    this.filePath = filePath;
    this.baseDir = baseDir;
    this.isDir = isDir;
    this.items = innerItems;
  }
}

export async function getItemFromDir(directory: string, itemName: string) {
  console.log({directory,itemName})
  const itemsInDir = await readdir(directory);
  console.log({ files: itemsInDir });

  const requestedItem = itemsInDir.find((f) => path.parse(f).name == itemName);

  return { requestedItem, itemsInDir};
}

export function directoryExist(fullPath: string) {
  return fs.existsSync(fullPath);
}

export function baseDirExists(filePath: string) {
  const { dir, name } = path.parse(filePath);
  return directoryExist(dir);
}


export async function getObjectInfo(dir: string,itemName:string) {
  const {requestedItem, itemsInDir} = await getItemFromDir(dir,itemName)
  const fullPath = path.join(dir,requestedItem ?? '') 

  return {fullPath,requestedItem,isDir:isDir(fullPath),itemsInDir}
}

export async function getDirStruct(dirPath:string) {
  return await readdir(dirPath);
}
