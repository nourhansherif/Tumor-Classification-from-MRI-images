
package Image;  

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream; 
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.JOptionPane;


public class img 
{
    public static File checkOnNormality(File input) throws IOException
    {
        try
        { 
            BufferedImage normalized = ImageIO.read(input);
            boolean isNormal = true;
        
            if ( !isGreyscale(normalized)) 
            { 
                normalized = tograyscale(normalized);
                isNormal = false;
            }
            
            if (normalized.getHeight() != 200 && normalized.getWidth() != 200) 
            {
                normalized = resizeImage(normalized);
                isNormal = false;
            }
            
            if(!checkFormat(input))
            { 
                isNormal = false;
            }
            
            if (isNormal) 
            {
                return input;
            }
        
            File file = new File(input.getParentFile().toString()+"/Normalized/");
            
            if (!file.exists()) 
            {
                file.mkdirs();
            }
            
            String filePath = input.getPath();
            String normalizedFilePath = filePath.substring(filePath.lastIndexOf("\\") + 1, 
                    filePath.lastIndexOf("."));
            File newFile = new File(file.getPath() + "\\" + normalizedFilePath + "_Normalized.jpg");
            
            Boolean result = ImageIO.write(normalized, "jpg", newFile);
            
            if (result) 
            {
                System.out.println("Image converted successfully.");
            } 
            else 
            {
                System.out.println("Could not convert image.");
            }
            
            return newFile;
        }
        catch(IOException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Exception", JOptionPane.ERROR_MESSAGE);
            
        }
        
        return null;
    }
    
    private static boolean isGreyscale(BufferedImage image)
    {
        int pixel,red, green, blue;
        
        for (int i = 0; i < image.getWidth(); i++)
        {
            for (int j = 0; j < image.getHeight(); j++) 
            {
                pixel = image.getRGB(i, j);
                red = (pixel >> 16) & 0xff;
                green = (pixel >> 8) & 0xff;
                blue = (pixel) & 0xff;
                
                if (red != green || green != blue ) 
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private static BufferedImage tograyscale (BufferedImage image) throws IOException
    {
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                Color color = new Color (image.getRGB(x, y));

                int red = (int) (color.getRed() * 0.299);
                int green  = (int)(color.getGreen() * 0.587);
                int blue = (int)(color.getBlue() * 0.114); 
                int sum  = red + green + blue;

                Color shadeofgray = new Color (sum, sum , sum);
                image.setRGB(x, y, shadeofgray.getRGB());
            }
        }
        
        return image;     
    }
       
    private static BufferedImage resizeImage(BufferedImage Image) throws IOException
    {
        BufferedImage normalized = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
        normalized.setData(Image.getData());
        Graphics2D g2 = normalized.createGraphics();
        g2.drawImage(Image,0,0,200,200,null);
        g2.dispose();
        
        return normalized;
    }
    
    private static boolean checkFormat(File imageToBeChecked) throws IOException
    {
        ImageInputStream iis =ImageIO.createImageInputStream(imageToBeChecked);
        Iterator<ImageReader>iterator = ImageIO.getImageReaders(iis);
        boolean flag = true;
        
        if (!iterator.hasNext()) 
        {
            throw new RuntimeException("No readers found for the image!");
        }
        
        ImageReader reader= iterator.next();
        System.out.println("File Format: " + reader.getFormatName());
        
        if (!("jpg".equals(reader.getFormatName().toLowerCase())) || 
                !("jpeg".equals(reader.getFormatName().toLowerCase())))
        {
            flag = false;
        }
        
        return flag;
    }
    
}
