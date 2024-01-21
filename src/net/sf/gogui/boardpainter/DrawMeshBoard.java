
package net.sf.gogui.boardpainter;

import java.awt.Point;
import net.sf.gogui.go.GoPoint;

/**
 *
 * @author tylerliu
 */
public abstract class DrawMeshBoard extends DrawBoard 
{
    public DrawMeshBoard(BoardStatus boardstatus, double[] v1, double[] v2)
    {
        super(boardstatus);
        vec1 = new double[]{v1[0], v1[1]};
        vec2 = new double[]{v2[0], v2[1]};
    }
    
    private double[] vec1;
    
    private double[] vec2;
    
    public GoPoint getPoint(Point point)
    {
        if (m_boardStatus.getFieldSize() == 0)
            return null;
        double min_distance = Double.MAX_VALUE;
        Point p = new Point();
        final int field_size = m_boardStatus.getFieldSize();
        for (int x = -1; x <= m_boardStatus.getSize(); x++)
        {
            for (int y = -1; y <= m_boardStatus.getSize(); y++)
            {
                Point loc = getLocation(x, y);
                Point center = new Point(loc.x + field_size / 2, loc.y + field_size / 2);
                double distance = Math.sqrt(Math.pow((point.getX() - center.getX()), 2) + Math.pow((point.getY() - center.getY()), 2));
                if (min_distance > distance)
                {
                    min_distance = distance;
                    p.x = x;
                    p.y = y;
                }
            }
        }
        
        if (p.x >= m_boardStatus.getSize() || p.y >= m_boardStatus.getSize())
            return null;
        if (p.x < 0 || p.y < 0)
            return null;
        return GoPoint.get(p.x, p.y);
    }
    
    private Point inverseTransformMatrix(int newX, int newY) 
    {
        double a = vec1[0];
        double b = vec2[0];
        double c = vec1[1];
        double d = vec2[1];

        double determinant = a * d - b * c;

        if (Math.abs(determinant) < 1e-6) 
        {
            throw new IllegalStateException("The transformation matrix is singular and cannot be inverted.");
        }

        double invA = d / determinant;
        double invB = -b / determinant;
        double invC = -c / determinant;
        double invD = a / determinant;

        int x = (int) Math.round(newX * invA + newY * invB);
        int y = (int) Math.round(newX * invC + newY * invD);

        return new Point(x, y);
    }
    
    public Point getLocation(int x, int y)
    {
        if (m_boardStatus.getFlipVertical())
            x = m_boardStatus.getSize() - 1 - x;
        if (! m_boardStatus.getFlipVertical())
            y = m_boardStatus.getSize() - 1 - y;
        Point point = transitionMatrx(x, y);
        return point;
    }
    
    private Point transitionMatrx(int x, int y)
    {
        double a = vec1[0];
        double b = vec2[0];
        double c = vec1[1];
        double d = vec2[1];
        Point point = new Point();        
        point.x = (int)((x * a + y * b) * m_boardStatus.getFieldSize() + m_boardStatus.getFieldOffset());
        point.y = (int)((x * c + y * d) * m_boardStatus.getFieldSize() + m_boardStatus.getFieldOffset());
        return point;
    }
}
