import java.awt.event.InputEvent;
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
        Editor editor = editors.get(visibleEditor);
        
        boolean control = (e.getModifiers() & KeyEvent.CTRL_MASK) != 0;
        
        if(e.getKeyCode() == KeyEvent.VK_ALT)
        {
            editor.mark();
            e.consume();
        }
        else if(control && e.getKeyCode() == KeyEvent.VK_V)
            editor.paste();
        else if((control && e.getKeyCode() == KeyEvent.VK_W) || e.getKeyCode() == KeyEvent.VK_UP)
            editor.up(1);
        else if((control && e.getKeyCode() == KeyEvent.VK_S) || e.getKeyCode() == KeyEvent.VK_DOWN)
            editor.down(1);
        else if((control && e.getKeyCode() == KeyEvent.VK_A) || e.getKeyCode() == KeyEvent.VK_LEFT)
            editor.left();
        else if((control && e.getKeyCode() == KeyEvent.VK_D) || e.getKeyCode() == KeyEvent.VK_RIGHT)
            editor.right();
        else if(control && e.getKeyCode() == KeyEvent.VK_L)
            editor.rightToken();
        else if(control && e.getKeyCode() == KeyEvent.VK_J)
            editor.leftToken();
        else if(control && e.getKeyCode() == KeyEvent.VK_I)
            editor.up(4);
        else if(control && e.getKeyCode() == KeyEvent.VK_K)
            editor.down(4);
        else if(e.getKeyCode() == KeyEvent.VK_HOME)
            editor.home();
        else if(e.getKeyCode() == KeyEvent.VK_END)
            editor.end();
        else if(e.getKeyCode() == KeyEvent.VK_DELETE)
            editor.delete();
        else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            editor.backspace();
        else if(e.getKeyCode() == KeyEvent.VK_ENTER)
            editor.enter();
        else if(java.lang.Character.isDefined(e.getKeyChar()) && (e.getModifiers() == 0 || e.getModifiers() == InputEvent.SHIFT_MASK))
            editor.type(e.getKeyChar());
        editor.repaint();
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {}
    @Override 
    public void keyTyped(KeyEvent e) {}
}
