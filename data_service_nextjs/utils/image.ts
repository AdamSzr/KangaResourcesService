import sharp from "sharp"
import { ImageSize } from "../models/ImageSize"

export function resizeImage(buffer: Buffer, size: string) {
    const targetSize = ImageSize.parse(size)
    if (!targetSize)
      return buffer
  
    return sharp(buffer).resize(targetSize.width, targetSize.height, { fit: "fill" }).toBuffer()
  }