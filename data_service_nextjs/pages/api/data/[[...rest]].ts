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
  MISSING_PATH_QUERY,
  NO_CONTENT,
  WRONG_PATH,
} from "../../../errors/errors";
import mime from 'mime-types'
import { directoryAnalizer as directoryAnalizer } from "../../../utils/directoryAnalizer";
import { ENABLE_FILE_DOWNLOAD } from "../../../settings";
import { resizeImage } from "../../../utils/image";

export const PUBLIC_DIR_ABS_PATH = path.join(process.cwd(), "public/data");

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  /** Full path that can be either dir of file */
  if (req.query.path == undefined)
    return res.status(404).json(MISSING_PATH_QUERY)

  const filePath = path.join(PUBLIC_DIR_ABS_PATH, ...req.query.path);
  const listDir = Boolean(req.query.list);
  const size = req.query.size as string;
  const { dir, base } = path.parse(filePath); //dir is a parent for base, base can contain either DIR name or FILE name

  try {
    if (!directoryExist(dir)) return res.status(404).send(WRONG_PATH);

    const { fullPath, requestedItem } =
      await getFileObjectInfo(dir, base);

    if (!requestedItem) return res.status(400).send(NO_CONTENT);

    if (isDir(fullPath))
      if (listDir) {
        const dirInfo = directoryAnalizer(filePath, await getDirStruct(filePath));
        return res.status(200).send(dirInfo);
      } else return res.status(400).send(LIST_DIRECTORY_ERROR);

    if (ENABLE_FILE_DOWNLOAD)
      res.setHeader("Content-Disposition", `attachment; filename=${requestedItem}`);

    let buffer = await readFile(fullPath);

    const mimeType = mime.lookup(requestedItem)
    if (mimeType && mimeType.startsWith('image'))
      buffer = await resizeImage(buffer, size) as Buffer

    return res.status(200).send(buffer);
  } catch (err: any) {
    return res.status(404).send(err.message);
  }
}
