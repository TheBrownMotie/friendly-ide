import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;


public class IDE extends JFrame implements KeyListener
{
    private static final long serialVersionUID = 1L;

    public static void main(String... args)
    {
        IDE ide = new IDE();
        ide.setSize(600, 600);
        ide.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ide.addKeyListener(ide);
        ide.setVisible(true);
    }
    
    private List<Editor> editors;
    private int visibleEditor;
    
    public IDE()
    {
        this.editors = new ArrayList<>();
        addEditor();
    }
    
    public void addEditor()
    {
        Editor e = new Editor();
        this.editors.add(e);
        this.visibleEditor = this.editors.size() - 1;
        this.add(e);
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        boolean control = (e.getModifiers() & KeyEvent.CTRL_MASK) != 0;
        
        if((control && e.getKeyCode() == KeyEvent.VK_W) || e.getKeyCode() == KeyEvent.VK_UP)
            editors.get(visibleEditor).up();
        else if((control && e.getKeyCode() == KeyEvent.VK_S) || e.getKeyCode() == KeyEvent.VK_DOWN)
            editors.get(visibleEditor).down();
        else if((control && e.getKeyCode() == KeyEvent.VK_A) || e.getKeyCode() == KeyEvent.VK_LEFT)
            editors.get(visibleEditor).left();
        else if((control && e.getKeyCode() == KeyEvent.VK_D) || e.getKeyCode() == KeyEvent.VK_RIGHT)
            editors.get(visibleEditor).right();
        else if(e.getKeyCode() == KeyEvent.VK_HOME)
            editors.get(visibleEditor).home();
        else if(e.getKeyCode() == KeyEvent.VK_END)
            editors.get(visibleEditor).end();
        else if(e.getKeyCode() == KeyEvent.VK_DELETE)
            editors.get(visibleEditor).delete();
        else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            editors.get(visibleEditor).backspace();
        else if(e.getKeyCode() == KeyEvent.VK_ENTER)
            editors.get(visibleEditor).enter();
        else if(java.lang.Character.isDefined(e.getKeyChar()) && e.getModifiers() == 0)
            editors.get(visibleEditor).type(e.getKeyChar());
        
        editors.get(visibleEditor).repaint();
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {}
    @Override 
    public void keyTyped(KeyEvent e) {}
}
