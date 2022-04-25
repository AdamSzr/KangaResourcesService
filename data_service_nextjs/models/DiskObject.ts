export type DiskObject = {
    type: "DIR" | "FILE"
    name: string
    createdAt: Date
    size: number
}