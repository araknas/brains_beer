package com.araknas.brains_beer.services;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Giedrius on 2017-11-17.
 */

@Service
public class KeyboardService {

    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");
    Object lock = new Object();
    public boolean isServiceRunning = false;

    public void createKeyListenerTask() {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (KeyboardService.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            logger.info("Pressed" + ke.getKeyChar());
                            break;

                        case KeyEvent.KEY_RELEASED:
                            break;
                    }
                    return false;
                }
            }
        });


        /*final ExecutorService pool = Executors.newFixedThreadPool(1);
        ExecutorService serviceRunner = Executors.newSingleThreadExecutor();
        isServiceRunning =  true;

        serviceRunner.execute(() -> {
            try {
                pool.execute(new TaskHandler());
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                isServiceRunning =  false;
            }

        });*/
    }

    class TaskHandler implements Runnable {
        TaskHandler(){
        }

        public void run() {
            synchronized (lock) {
                manageTask();
            }
        }
    }

    private void manageTask() {

        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
        //final EventHandler eventHandler = new EventHandler(this);

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent event) {
                //eventHandler.handleKeyPressEvent(event);
                logger.info("Pressed" + event.getKeyChar());
            }
            @Override
            public void keyReleased(GlobalKeyEvent event) {
                //eventHandler.handleKeyReleaseEvent(event);
            }
        });


        while (isServiceRunning) {
            try {
                // listening
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
                isServiceRunning =  false;
            }
            finally {
                keyboardHook.shutdownHook();
            }
        }
    }

}
