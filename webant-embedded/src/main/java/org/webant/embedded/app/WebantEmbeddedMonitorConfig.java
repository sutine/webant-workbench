package org.webant.embedded.app;

import org.apache.commons.lang3.StringUtils;
import org.webant.worker.WorkerConsole;
import org.webant.worker.config.ConfigManager;
import org.webant.worker.config.SiteConfigFileMonitor;
import org.webant.worker.config.TaskConfigFileMonitor;
import org.webant.worker.console.ConsoleOperation;

import java.net.URL;
import java.util.Scanner;

/**
 * run a stand alone worker to debug
 */
public class WebantEmbeddedMonitorConfig {
    private static ConsoleOperation console = new ConsoleOperation();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("starting webant ...");

        ConfigManager.loadWorkerConfig("worker.xml");

        // load and submit tasks, "task" and "site" are two directory contains config files in classes directory
        URL taskUrl = ClassLoader.getSystemResource("task");
        URL siteUrl = ClassLoader.getSystemResource("site");
        String suffix = ".json";
        WorkerConsole.getWorkerConsole().loadAllSiteConfig(siteUrl.getPath(), suffix);
        WorkerConsole.getWorkerConsole().loadAllTaskConfig(taskUrl.getPath(), suffix);

        // monitor config directory, if config file modified, auto reload
        SiteConfigFileMonitor.getFileMonitor().monitor(siteUrl.getPath(), suffix, 500);
        TaskConfigFileMonitor.getFileMonitor().monitor(taskUrl.getPath(), suffix, 500);

        new Thread(WebantEmbeddedMonitorConfig::console).start();
    }

    private static void console() {
        String promotion = "Webant # ";

        while (true) {
            System.out.print(promotion);

            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.nextLine();
            if (cmd == null || cmd.equals(""))
                continue;

            cmd = cmd.trim().toLowerCase();

            try {
                if (cmd.startsWith("start")) {
                    String[] items = cmd.split(" ");
                    if (items.length == 1) {
                        console.start();
/*
                        String[][] result = console.start();
                        for (String[] texts : result) {
                            for (String text : texts) {
                                System.out.println(text);
                            }
                        }
*/
                    }
                    else if (items.length == 2) {
                        console.start(items[1]);
/*
                        String[] result = console.start(items[1]);
                        for (String item : result) {
                            System.out.println(item);
                        }
*/
                    }
                    else if (items.length >= 3) System.out.println(console.start(items[1], items[2]));
                }
                else if (cmd.startsWith("stop")) {
                    String[] items = cmd.split(" ");
                    if (items.length == 1) {
                       console.stop();
/*
                        String[][] result = console.stop();
                        for (String[] texts : result) {
                            for (String text : texts) {
                                System.out.println(text);
                            }
                        }
*/
                    }
                    else if (items.length == 2) {
                        console.stop(items[1]);
/*
                        String[] result = console.stop(items[1]);
                        for (String item : result) {
                            System.out.println(item);
                        }
*/
                    }
                    else if (items.length >= 3) System.out.println(console.stop(items[1], items[2]));
                }
                else if (cmd.startsWith("pause") || cmd.startsWith("pause")) {
                    String[] items = cmd.split(" ");
                    if (items.length == 1) {
                        console.pause();
/*
                        String[][] result = console.pause();
                        for (String[] texts : result) {
                            for (String text : texts) {
                                System.out.println(text);
                            }
                        }
*/
                    }
                    else if (items.length == 2) {
                        console.pause(items[1]);
/*
                        String[] result = console.pause(items[1]);
                        for (String item : result) {
                            System.out.println(item);
                        }
*/
                    }
                    else if (items.length >= 3) System.out.println(console.pause(items[1], items[2]));
                }
                else if (cmd.startsWith("reset") || cmd.startsWith("recrawl")) {
                    String[] items = cmd.split(" ");
                    if (items.length == 1) {
                        console.reset();
/*
                        String[][] result = console.recrawl();
                        for (String[] texts : result) {
                            for (String text : texts) {
                                System.out.println(text);
                            }
                        }
*/
                    }
                    else if (items.length == 2) {
                        console.reset(items[1]);
/*
                        String[] result = console.recrawl(items[1]);
                        for (String item : result) {
                            System.out.println(item);
                        }
*/
                    }
                    else if (items.length >= 3) System.out.println(console.reset(items[1], items[2]));
                }
                else if (cmd.startsWith("p") || cmd.startsWith("progress")) {
                    String[] items = cmd.split(" ");
                    if (items.length == 1) System.out.println(console.progress());
                    else if (items.length == 2) System.out.println(console.progress(items[1]));
                    else if (items.length >= 3) System.out.println(console.progress(items[1], items[2]));
                } else if (cmd.startsWith("submit task")) {
                    String path = StringUtils.substringAfter(cmd, "submit task").trim();
                    if (StringUtils.isNotBlank(path)) {
                        console.submitTask(path);
                        System.out.println("loading task config...");
                    } else {
                        System.out.println("config file name can not be null!");
                    }
                } else if (cmd.startsWith("submit site")) {
                    String path = StringUtils.substringAfter(cmd, "submit site").trim();
                    if (StringUtils.isNotBlank(path)) {
                        console.submitSite(path);
                        System.out.println("loading site config...");
                    } else {
                        System.out.println("config file name can not be null!");
                    }
                }
                else if ("shutdown".equalsIgnoreCase(cmd))
                    System.out.println(console.exit());
                else if ("exit".equalsIgnoreCase(cmd))
                    break;
                else {
                    System.out.println("unknowned command!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
