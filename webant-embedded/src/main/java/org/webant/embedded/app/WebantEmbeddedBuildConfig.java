package org.webant.embedded.app;

import org.apache.commons.lang3.StringUtils;
import org.webant.worker.config.*;
import org.webant.worker.console.ConsoleOperation;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * run a stand alone worker to debug
 */
public class WebantEmbeddedBuildConfig {
    private static ConsoleOperation console = new ConsoleOperation();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("starting webant ...");

        ConfigManager.loadWorkerConfig("worker.xml");

        TaskConfig taskConfig = buildTaskConfig();
        console.submit(taskConfig);

        new Thread(WebantEmbeddedBuildConfig::console).start();
    }


    private static TaskConfig buildTaskConfig() {
        // build site config by SiteConfigBuilder
        SiteConfig mahuaConfig = buildSiteConfig();
        console.submit(mahuaConfig);

        // build site config by load config file
        URL budejieUrl = ClassLoader.getSystemResource("site/budejie.json");
        SiteConfig budejieConfig = new SiteConfigBuilder()
                .loadSiteConfig(budejieUrl.getPath())
                .build();
        console.submit(budejieConfig);

        return new TaskConfigBuilder()
                .id("task_mahua")
                .name("麻花笑话网")
                .description("麻花笑话网")
                .priority(4)
                .sites(new String[] {mahuaConfig.id(), budejieConfig.id()})
                .build();
    }

    private static SiteConfig buildSiteConfig() {
        Map<String, Object> linkProviderParams = new HashMap<>();
        linkProviderParams.put("url", "jdbc:h2:./data/h2/webant;MODE=MYSQL");
        linkProviderParams.put("username", "webant");
        linkProviderParams.put("password", "webant");
        linkProviderParams.put("batch", 20);

        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpHeaders.put("Accept-Encoding", "gzip, deflate, sdch");
        httpHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
        httpHeaders.put("Cache-Control", "max-age=0");
        httpHeaders.put("Cookie", "UM_distinctid=15bde680426215-0dd1f2a838eeff-67f1a39-144000-15bde680427480; MH_UP_JOKES=%2C1664724%2C; CNZZDATA1000005254=1002886654-1494081766-%7C1494781843; Hm_lvt_035fdfc559009c734b7c7e6bdd54bc20=1494085011; Hm_lpvt_035fdfc559009c734b7c7e6bdd54bc20=1494783080");
        httpHeaders.put("Proxy-Connection", "keep-alive");
        httpHeaders.put("Upgrade-Insecure-Requests", "1");
        httpHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");

        return new SiteConfigBuilder().id("site_mahua")
                .name("麻花笑话网")
                .description("麻花笑话网")
                .seeds(new String[]{"http://www.mahua.com"})
                .interval(3000)
                .priority(4)
                .linkProvider(new LinkProviderBuilder()
                        .className("org.webant.worker.link.H2LinkProvider")
                        .params(linkProviderParams)
                        .build())
                .http(new HttpConfigBuilder()
                        .connectTimeout(3000)
                        .socketTimeout(5000)
                        .encoding("UTF-8")
                        .contentType("text/html")
                        .retryTimes(3)
                        .cycleRetryTimes(2)
                        .proxy(false)
                        .headers(httpHeaders)
                        .build())
                .processors(new ProcessorConfig[]{
                        new PageProcessorBuilder().regex("http://www.mahua.com/?")
                                .build(),
                        new PageProcessorBuilder().regex("http://www.mahua.com/newjokes/index_\\d*.htm")
                                .build(),
                        new PageProcessorBuilder().regex("http://www.mahua.com/xiaohua/\\d*.htm")
                                .className("org.webant.plugin.mahua.processor.MahuaDetailProcessor")
                                .build()
//                .stores(new HashMap<String, String>[] {
//                })
                })
                .build();
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
