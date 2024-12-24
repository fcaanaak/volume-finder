package volumefinder;

import java.util.ArrayList;
import javax.swing.JButton;
import java.util.List;

public class ButtonManager {

    private List<JButton>buttonList;


    public ButtonManager(List<JButton>buttonList) {
        this.buttonList = buttonList;
    }

    // Disable all buttons except the one passed in
    public void disableOthers(JButton survivorButton) {

        for (JButton button: buttonList) {

            if (!button.equals(survivorButton)) {
                button.setEnabled(false);
            }
        }

    }

    public void enableAll() {
        buttonList.forEach(button->button.setEnabled(true));
    }


}

