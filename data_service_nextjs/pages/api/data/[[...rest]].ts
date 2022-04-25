// Next.js API route support: https://nextjs.org/docs/api-routes/introduction
import type { NextApiRequest, NextApiResponse } from "next";
import path from "path";
import {
  directoryExist,
  getDirStruct,
  getObjectInfo as getFileObjectInfo,
  isDir,
} from "../../../utils/file";
import { readFile } from "fs/promises";
import {
  LIST_DIRECTORY_ERROR,
  NO_CONTENT,
  WRONG_PATH,
} from "../../../errors/errors";
import { catalogueAnalizer as directoryAnalizer } from "../../../utils/directoryAnalizer";

export const PUBLIC_DIR_ABS_PATH = path.join(process.cwd(), "public/data");

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  /** Full path that can be either dir of file */ 
  const filePath = path.join(PUBLIC_DIR_ABS_PATH, ...req.query.rest??"");
  const listDir = req.query.list;
  try {
    const { dir, base } = path.parse(filePath);

    if (!directoryExist(dir)) return res.status(404).send(WRONG_PATH);

    const { fullPath, requestedItem } =
      await getFileObjectInfo(dir, base);

    if (!requestedItem) return res.status(400).send(NO_CONTENT);

    if (isDir(fullPath))
      if (listDir) {
        const dirInfo = directoryAnalizer(filePath, await getDirStruct(filePath));
        return res.status(200).send(dirInfo);
      } else return res.status(400).send(LIST_DIRECTORY_ERROR);

    const buffer = await readFile(fullPath);

    res.setHeader(
      "Content-Disposition",
      `attachment; filename=${requestedItem}`
    ); // set proper (same as on server) file with ext for saving purpose
    return res.status(200).send(buffer);
  } catch (err: any) {
    return res.status(404).send(err.message);
  }
}
