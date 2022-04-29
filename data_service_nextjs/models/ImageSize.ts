export class ImageSize{
    size:any
    width:any
    height:any
    private constructor(size:string,width:number,height:number){
        this.size = size
        this.width = width
        this.height = height
    }
    
    public static parse(size:string){
        const x = size.split('x')
        const width = Number(x[0])
        const height = Number(x[1])
        if(isNaN(width) || isNaN(height))
        return undefined
        
        return new ImageSize(size,width,height)
    }
}