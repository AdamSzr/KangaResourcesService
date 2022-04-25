// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";
import path from "path";
import {
  baseDirExists,
  directoryExist,
  getDirStruct,
  getObjectInfo as getFileObjectInfo,
} from "../../../utils/file";
import { readFile } from "fs/promises";
import { fstat, statSync } from "fs";
import { LIST_DIRECTORY_ERROR, NO_CONTENT, WRONG_PATH } from "../../../errors/errors";
 
export const PUBLIC_DIR_ABS_PATH = path.join(process.cwd(), "public/data");

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  const filePath = path.join(PUBLIC_DIR_ABS_PATH,...req.query.rest);
  const listDir = req.query.list;

  try {
    if (!baseDirExists(filePath))
      return res.status(400).send(WRONG_PATH);

    const { dir, base} = path.parse(filePath);
    const { fullPath, requestedItem, isDir, itemsInDir } =
      await getFileObjectInfo(dir, base);

    if(!requestedItem)
      return res.status(400).send(NO_CONTENT)

    if (isDir)
      if (listDir) return res.status(400).send(await getDirStruct(fullPath));
      else return res.status(400).send(LIST_DIRECTORY_ERROR);

    const buffer = await readFile(fullPath);

    res.setHeader(
      "Content-Disposition",
      `attachment; filename=${requestedItem}`
    ); // set proper (same as on server) file with ext for saving purpose
    res.status(200).send(buffer);
  } catch (err: any) {
    return res.status(404).send(err.message);
  }
}
