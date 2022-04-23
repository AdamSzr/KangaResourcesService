import { readdir } from "fs/promises";
import fs from "fs";
import path from "path";

const PUBLIC_DIR_ABS_PATH = path.join(process.cwd(), "public/data");

export async function fileExists(filePath: string) {
  const file = path.join(PUBLIC_DIR_ABS_PATH, filePath);
  const { dir, name } = path.parse(file);

  let files = await readdir(dir);
  // console.log({ name, dir, files });

  const wanted = files.find((f) => path.parse(f).name == name);
  
  if (wanted) return path.join(dir, wanted);
}
