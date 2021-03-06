/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  Part of the Processing project - http://processing.org

  Copyright (c) 2004-11 Ben Fry and Casey Reas
  Copyright (c) 2001-04 Massachusetts Institute of Technology

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2
  as published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software Foundation,
  Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package processing.app.contrib;

import java.io.*;
//import java.net.*;
import java.util.*;

import processing.app.Base;
//import processing.app.Base;
import processing.app.Editor;
import processing.app.tools.Tool;


public class ToolContribution extends InstalledContribution implements Tool {
//  static String propertiesFileName = "tool.properties";

//  private String className;
  private Tool tool;


  static public ToolContribution load(File folder) {
    try {
      return new ToolContribution(folder);
    } catch (IgnorableException ig) {
      Base.log(ig.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  private ToolContribution(File folder) throws Exception {
    super(folder);

    String className = initLoader(null);
    if (className != null) {
      Class<?> toolClass = loader.loadClass(className);
      tool = (Tool) toolClass.newInstance();
    }

//    File toolDirectory = new File(folder, "tool");
//    // add dir to classpath for .classes
//    //urlList.add(toolDirectory.toURL());
//
//    // add .jar files to classpath
//    File[] archives = Base.listJarFiles(toolDirectory);
////    File[] archives = toolDirectory.listFiles(new FilenameFilter() {
////      public boolean accept(File dir, String name) {
////        return (name.toLowerCase().endsWith(".jar") ||
////            name.toLowerCase().endsWith(".zip"));
////      }
////    });
//
//    if (archives != null && archives.length > 0) {
//      try {
//        URL[] urlList = new URL[archives.length];
//        for (int j = 0; j < urlList.length; j++) {
//          urlList[j] = archives[j].toURI().toURL();
//        }
//        loader = new URLClassLoader(urlList);
//
//        for (int j = 0; j < archives.length; j++) {
//          className = findClassInZipFile(folder.getName(), archives[j]);
//          if (className != null) break;
//        }
//      } catch (MalformedURLException e) { }
//    }
//
//    /*
//    // Alternatively, could use manifest files with special attributes:
//    // http://java.sun.com/j2se/1.3/docs/guide/jar/jar.html
//    // Example code for loading from a manifest file:
//    // http://forums.sun.com/thread.jspa?messageID=3791501
//    File infoFile = new File(toolDirectory, "tool.txt");
//    if (!infoFile.exists()) continue;
//
//    String[] info = PApplet.loadStrings(infoFile);
//    //Main-Class: org.poo.shoe.AwesomerTool
//    //String className = folders[i].getName();
//    String className = null;
//    for (int k = 0; k < info.length; k++) {
//      if (info[k].startsWith(";")) continue;
//
//      String[] pieces = PApplet.splitTokens(info[k], ": ");
//      if (pieces.length == 2) {
//        if (pieces[0].equals("Main-Class")) {
//          className = pieces[1];
//        }
//      }
//    }
//     */
  }


  static protected List<File> discover(File folder) {
    File[] folders = listCandidates(folder, "tool");
    if (folders == null) {
      return new ArrayList<File>();
    } else {
      return Arrays.asList(folders);
    }
  }


  static public ArrayList<ToolContribution> loadAll(File toolsFolder) {
    List<File> list = discover(toolsFolder);
    ArrayList<ToolContribution> outgoing = new ArrayList<ToolContribution>();
    for (File folder : list) {
      try {
        ToolContribution tc = load(folder);
        if (tc != null) {
          outgoing.add(tc);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return outgoing;
  }


//  /**
//   * @return true if a Tool class of the expected name was found in this tool's
//   *         classpath
//   */
//  private boolean isValid() {
//    return className != null;
//  }


//  /**
//   * Loads the tool, making it impossible (on Windows) to move the files in the
//   * classpath without restarting the PDE.
//   */
//  public void initializeToolClass() throws Exception {
//    Class<?> toolClass = Class.forName(className, true, loader);
//    tool = (Tool) toolClass.newInstance();
//  }


//  /**
//   * Searches and returns a list of tools found in the immediate children of the
//   * given folder.
//   * @param doInitializeToolClass
//   *          true if tools should be initialized before they are returned.
//   *          Tools that failed to initialize for whatever reason are not
//   *          returned
//   */
//  public static ArrayList<ToolContribution> list(File folder, boolean doInitializeToolClass) {
//    ArrayList<File> toolsFolders = ToolContribution.discover(folder);
//
//    ArrayList<ToolContribution> tools = new ArrayList<ToolContribution>();
//    for (File toolFolder : toolsFolders) {
//      final ToolContribution tool = ToolContribution.load(toolFolder);
//      if (tool != null) {
//        try {
//          if (doInitializeToolClass) {
//            tool.initializeToolClass();
//          }
//          tools.add(tool);
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//    }
//    return tools;
//  }


//  static protected ArrayList<File> discover(File folder) {
//    ArrayList<File> tools = new ArrayList<File>();
//    discover(folder, tools);
//    return tools;
//  }
//
//
//  static protected void discover(File folder, ArrayList<File> toolFolders) {
//    File[] folders = folder.listFiles(new FileFilter() {
//      public boolean accept(File folder) {
//        if (folder.isDirectory()) {
//          //System.out.println("checking " + folder);
//          File subfolder = new File(folder, "tool");
//          return subfolder.exists();
//        }
//        return false;
//      }
//
////      private boolean toolAlreadyExists(File folder) {
////        boolean exists = true;
////        for (ToolContribution contrib : tools) {
////          if (contrib.getFolder().equals(folder)) {
////            exists = false;
////          }
////        }
////        return exists;
////      }
//    });
//
//    if (folders != null) {
//      for (int i = 0; i < folders.length; i++) {
//        Tool tool = ToolContribution.load(folders[i]);
//
//        if (tool != null)
//          toolFolders.add(folders[i]);
//      }
//    }
//  }


  public void init(Editor editor) {
    tool.init(editor);
  }


  public void run() {
    tool.run();
  }


  public String getMenuTitle() {
    return tool.getMenuTitle();
  }


  public Type getType() {
    return Type.TOOL;
  }
}