package org.bumble.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bumble.base.BasicConst;
import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class ReflectUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
	
	private static final LocalConfigHolder localCofigHolder = LocalConfigHolder.getInstance();
	
	/**
     * Indicates that this implementation will be ignored by ReflectUtil 
     * getInstance mechanism.
     * <p>
     */
	@Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IgnoreThisImplementation {
        // no value
    } 
	
	/**
	 * Get instance of a class which implements certain interface
	 * <p>
	 * if there is no class implements this interface<br>
	 * or there are multiple implementations on the class path<br>
	 * null will be returned.<br>
	 * <p>
	 * @param interfaze
	 * @return
	 */
	public static <T extends Object> T getInstance(Class interfaze) {
		return getInstance(interfaze, new Object[] {});
	}
	
	/**
	 * Get instance of a class with constructor which implements certain interface
	 * <p>
	 * if there is no class implements this interface<br>
	 * or there are multiple implementations on the class path<br>
	 * null will be returned.<br>
	 * <p>
	 * @param interfaze
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T getInstance(Class interfaze, Object... args) {
		T instance = null;
		try {
			String interfaceName = interfaze.getName();
			List<Class> classes = getImplementClasses4Interface(interfaze);
			if (classes.isEmpty()) {
				logger.warn("No implementation for interface [" + interfaceName + "]");
				return null;
			}
			Class clazz = null;
			if (classes.size() > 1) {
				logger.warn("Multiple implementations found for interface [" + interfaceName + "]");
				String interfaceMapperKey = String.join(BasicConst.POINT, LocalConfigConst.Reflect.MAPPERS, interfaceName);
				
				String implementClassName = LocalConfigHolder.getInstance().getConfig(interfaceMapperKey);
				for (Class clz : classes) {
					if (clz.getName().equals(implementClassName)) {
						clazz = clz;
						break;
					}
				}
			}
			if (clazz == null) {
				clazz = classes.get(0);
			}
			logger.debug("[" + clazz.getName() + "] is used as implementation for [" + interfaceName + "]");
			
			if (args == null || args.length == 0) {
				instance = (T) clazz.newInstance();
			} else {
				List<Class> argList = new ArrayList<Class>();
				for (Object arg : args) {
					Class argClass = arg.getClass();
					argList.add(argClass);
				}
				Class[] argClasses = (Class[]) argList.toArray();
				
				Constructor constructor = clazz.getDeclaredConstructor(argClasses);
				constructor.setAccessible(true);
				instance = (T) constructor.newInstance(args);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	/**
	 * Get class list which implements an interface
	 * <p>
	 * It will lookup the classpath and the sub classpath of the given interface
	 * <p>
	 * @param interfaze
	 * @return
	 */
	public static List<Class> getImplementClasses4Interface(Class interfaze) {
		String packageName = interfaze.getPackage().getName();
		return getImplementClasses4Interface(interfaze, packageName);
	}
	
	/**
	 * Get class list which implements an interface
	 * <p>
	 * It will lookup the classpath and the sub classpath of the given packageName
	 * <p>
	 * @param interfaze
	 * @param packageName
	 * @return
	 */
	public static List<Class> getImplementClasses4Interface(Class interfaze, String packageName) {
		ArrayList<Class> classes = new ArrayList<Class>();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	        String path = packageName.replace(".", "/");
	        
	        Enumeration<URL> resources = classLoader.getResources(path);
	        List<File> dirs = new ArrayList<File>();
	        while (resources.hasMoreElements()) {
	            URL resource = resources.nextElement();
	            dirs.add(new File(resource.getFile()));
	        }
	        
	        for (File directory : dirs) {
	        	List<Class> classesFound = findClass(directory, packageName, interfaze);
	        	for (Class classFound : classesFound) {
	        		if (!classes.contains(classFound)) {
	        			classes.add(classFound);
	        		}
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	@SuppressWarnings("unchecked")
	private static void pickClass4Interface(List<Class> classes, String packageName, String className, Class interfaze) throws Exception {
		if (className.contains("$")) {
			return;
		}
		try {
			Class clazz = (Class) Class.forName(packageName + "." + className);
	    	if (Modifier.isInterface(clazz.getModifiers()))
	    		return;
	    	if (Modifier.isAbstract(clazz.getModifiers()))
	    		return;
	    	
	    	if (interfaze.isAssignableFrom(clazz)) {
	    		if (clazz.isAnnotationPresent(IgnoreThisImplementation.class)) {
	    			return;
	    		}
	    		
	    		Method[] methods = clazz.getMethods();
	    		Boolean ignore = false;
	    		for (Method method : methods) {
	    			if (method.getName().equals("ignoreThisImplementation")) {
	    				ignore = true;
	    				break;
	    			}
	    		}
	    		if (ignore) {
	    			return;
	    		}
	    		if (!classes.contains(clazz))
	    			classes.add(clazz);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void pickClass4InterfaceInFileSystem(File directory, List<Class> classes, String packageName, Class interfaze) throws Exception {
		File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClass(file, packageName + "." + file.getName(), interfaze));
            } else if (file.getName().endsWith(".class")) {
            	String className = file.getName().substring(0, file.getName().length() - 6);
            	pickClass4Interface(classes, packageName, className, interfaze);
            }
        }
	}
	
	private static void inputStream2file(InputStream ins, File file) throws Exception{
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}
	
	private static SimpleClass getSimpleClass(String path, String packagePath) {
		if (!path.endsWith(".class")) {
			return null;
		}
		if (path.contains("$")) {
			return null;
		}
		// Case for spring boot
		path = path.replace("BOOT-INF/classes/", "");
		String className = path.substring(path.lastIndexOf("/") + 1, path.length() - 6);
		
		String pathWithoutFile = path.substring(0, path.lastIndexOf("/"));
		String calcedPackagePath = pathWithoutFile.replaceAll("\\/", ".");
		
		if (!calcedPackagePath.contains(packagePath)) {
			return null;
		}
		SimpleClass sc = new SimpleClass(calcedPackagePath, className);
		return sc;
	}
	
	private static class SimpleClass {
		SimpleClass(String packge, String name) {
			this.packge = packge;
			this.name = name;
		}
		private String packge = null;
		public String getPackge() {
			return packge;
		}
		public String getName() {
			return name;
		}
		private String name = null;
	}
	
	private static void pickClass4InterfaceInJar(File directory, List<Class> classes, String packageName, Class interfaze) throws Exception {
		JarFile jarFile = null;
		try {
			
			String jarPath = directory.getPath().replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
			
	        jarFile = new JarFile(jarPath);
	        
	        Enumeration<JarEntry> jarEntries = jarFile.entries();
	        JarEntry jarEntry = null;
	        while (jarEntries.hasMoreElements()) {
	        	jarEntry = jarEntries.nextElement();
	        	
	        	String jarEntryPath = jarEntry.getName();
	        	
	        	if (jarEntryPath.endsWith(".jar")) {       		
	        		InputStream is = jarFile.getInputStream(jarEntry);
	        		String tmpFilePath = "";
	        		String tmpFileName = "t.tmp";
	        		if (localCofigHolder.getConfig(LocalConfigConst.Reflect.USE_MULTIPLE_TMP_FILE).equals("true")) {
	        			tmpFileName += UuidUtil.uuid() + System.currentTimeMillis();
	        		}
	        		
	        		String folderName = localCofigHolder.getConfig(LocalConfigConst.Reflect.FOLDER_NAME);
	        		
	        		if (SystemUtil.isWindowsOs()) {
	        			tmpFilePath = localCofigHolder.getConfig(LocalConfigConst.Reflect.PATH_KEY_WIN);
	        		} else {
	        			tmpFilePath = localCofigHolder.getConfig(LocalConfigConst.Reflect.PATH_KEY_LINUX);
	        		}
	        		tmpFilePath += File.separator + folderName + File.separator;
	        		
	        		File path = new File(tmpFilePath);
	        		if (!path.exists()) {
	        			path.mkdirs();
	        		}
	        		
	        		File file = new File(tmpFilePath + tmpFileName);
	        		inputStream2file(is, file);
	        		pickClass4InterfaceInJar(file, classes, packageName, interfaze);
	        		
	        		if (localCofigHolder.getConfig(LocalConfigConst.Reflect.DEL_TMP_FILE).equals("true")) {
	        			file.delete();
	        		}
	        	} else if (jarEntryPath.endsWith(".class")) {
	        		SimpleClass sc = getSimpleClass(jarEntryPath, packageName);
	        		if (sc != null) {
			        	pickClass4Interface(classes, sc.getPackge(), sc.getName(), interfaze);
	        		}
	        	}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jarFile != null) {
				jarFile.close();
			}
		}
		
	}
	
	private static List<Class> findClass(File directory, String packageName, Class interfaze) throws Exception {
        List<Class> classes = new ArrayList<Class>();
        if (directory.exists()) {
        	pickClass4InterfaceInFileSystem(directory, classes, packageName, interfaze);
        } else {
        	pickClass4InterfaceInJar(directory, classes, packageName, interfaze);
        }
        return classes;
	}
}
