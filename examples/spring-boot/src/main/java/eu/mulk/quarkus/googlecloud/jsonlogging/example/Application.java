// SPDX-FileCopyrightText: © 2021 Matthias Andreas Benkard <code@mail.matthias.benkard.de>
//
// SPDX-License-Identifier: GPL-3.0-or-later

package eu.mulk.quarkus.googlecloud.jsonlogging.example;

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  static {
    TomcatURLStreamHandlerFactory.disable();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
