package management;

import java.lang.management.BufferPoolMXBean;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Map;

/**
 * Java Management应用示例（另有JMX是其扩展实现）
 * management常用于对jvm的管理，如过去系统信息、java进程信息、内存使用情况、gc情况等
 * @author wangqiang
 */
public class ManagementBasic {

	public static void main(String[] args) {
		fetchCurrentProcessInfo();
		fetchMemoryMXBean();
		fetchMemoryManagerMxBeans();
		fetchMemoryPoolMXBeans();
		fetchClassLoadingMXBean();
		fetchGarbageCollectionMXBean();
		fetchThreadMXBean();
		fetchOperatingSystemMXBean();
		fetchCompilationMXBean();
		fetchBufferPoolMXBean();
	}

	// 获取当前应用的进程的信息（进程名，vm信息，系统变量，classpath等）
	private static void fetchCurrentProcessInfo() {
		System.out.println("\n[fetchCurrentProcessInfo]");
		/** 
		 * 进程信息
		 * pname的格式是 {pid}@hostname 40214@wangqiangdeMacBook-Pro.local
		 */
		RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();
		
		String pname = mxbean.getName();
		System.out.println(String.format("processName: %s", pname));
		// 截取进程号
		int currentProcessId = Integer.valueOf(pname.substring(0, pname.indexOf("@")));
		System.out.println(String.format("processId: %d", currentProcessId));
		
		// 获取系统变量
		Map<String, String> properties = mxbean.getSystemProperties();
		System.out.println("\n>>>>> print system properties begin");
		properties.forEach((k,v) -> {System.out.println(String.format("%s=>%s", k, v));});
		System.out.println(">>>>> print system properties end\n");
		
		// 进程启动存活时间
		System.out.printf("startTime = %d, upTime = %d\n", mxbean.getStartTime(), mxbean.getUptime());
		// 进程规格信息
		// specName = Java Virtual Machine Specification, specVendor = Oracle Corporation, specVersion = 1.8
		System.out.printf("specName = %s, specVendor = %s, specVersion = %s\n",
				mxbean.getSpecName(), mxbean.getSpecVendor(), mxbean.getSpecVersion());

		// 虚拟机信息
		// vmName = Java HotSpot(TM) 64-Bit Server VM, vmVendor = Oracle Corporation, vmVesion = 25.211-b12
		System.out.printf("vmName = %s, vmVendor = %s, vmVesion = %s\n",
				mxbean.getVmName(), mxbean.getVmVendor(), mxbean.getVmVersion());

		// classpath相关
		System.out.println(">>> classpath = " + mxbean.getClassPath());
		System.out.println(">>> bootClassPath = " + mxbean.getBootClassPath());
		System.out.println(">>> libraryPath = " + mxbean.getLibraryPath());
		System.out.println();
		
		// 参数信息
		List<String> inputArguments = mxbean.getInputArguments();
		inputArguments.forEach(x -> System.out.println(">>> arg: " + x));
	}

	// 查看当前进程内存使用情况
	private static void fetchMemoryMXBean() {
		System.out.println("\n[fetchMemoryMXBean]");
		MemoryMXBean mxbean = ManagementFactory.getMemoryMXBean();
		// 堆内存的使用
		MemoryUsage usage = mxbean.getHeapMemoryUsage();
		// init = 268435456(262144K) used = 2684392(2621K) committed = 257425408(251392K) max = 3817865216(3728384K)
		System.out.println(usage);
		
		// 非堆内存的使用
		MemoryUsage nonUsage = mxbean.getNonHeapMemoryUsage();
		System.out.println(nonUsage);
		ManagementFactory.getMemoryManagerMXBeans();
	}
	
	// 查看内存管理相关信息
	private static void fetchMemoryManagerMxBeans() {
		System.out.println("\n[fetchMemoryManagerMxBeans]");
		List<MemoryManagerMXBean> mxbeans = ManagementFactory.getMemoryManagerMXBeans();
		for (MemoryManagerMXBean mxbean : mxbeans) {
			String name = mxbean.getName();
			String poolNames = String.join(";", mxbean.getMemoryPoolNames());
			System.out.printf(">>> MemoryManager: name = %s, poolNames = %s\n", name, poolNames);
		}
		ManagementFactory.getMemoryPoolMXBeans();
	}
	
	// 查看内存分区使用的相关信息
	private static void fetchMemoryPoolMXBeans() {
		System.out.println("\n[fetchMemoryPoolMXBeans]");
		List<MemoryPoolMXBean> mxbeans = ManagementFactory.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean mxbean : mxbeans) {
			// getUsage: 内存使用预估
			System.out.printf(">>> name = %s, type = %s, usage_init = %d, usage_max = %d, usage_used = %d,"
					+ " usage_threshold = %d, usage_threshold_count = %d", 
					mxbean.getName(), mxbean.getType().name(), mxbean.getUsage().getInit(),
					mxbean.getUsage().getMax(), mxbean.getUsage().getUsed(),
					0, 0);
			// getCollectionUsage: 最近一次gc依赖以来内存使用情况
			if (mxbean.isCollectionUsageThresholdSupported()) {
				MemoryUsage cusage = mxbean.getCollectionUsage();
				System.out.printf(", collectionUsage_init = %d, collectionUsage_max = %d, collectionUsage_used = %d",
						cusage.getInit(), cusage.getMax(), cusage.getUsed());
			}
			
			// getPeakUsage: 内存峰值情况
			MemoryUsage pusage = mxbean.getPeakUsage();
			System.out.printf(", peakUsage_init = %d, peakUsage_max = %d, peakUsage_used = %d",
					pusage.getInit(), pusage.getMax(), pusage.getUsed());
			System.out.println();
		}
	}

	// 获取类记载相关信息
	private static void fetchClassLoadingMXBean() {
		System.out.println("\n[fetchClassLoadingMXBean]");
		ClassLoadingMXBean mxbean = ManagementFactory.getClassLoadingMXBean();
		int loadedClassCount = mxbean.getLoadedClassCount();
		long unloadedClassCount = mxbean.getUnloadedClassCount();
		System.out.println(String.format("loadedClassCount = %d, unloadedClassCount = %d",
				loadedClassCount, unloadedClassCount));
	}

	// gc相关的信息
	private static void fetchGarbageCollectionMXBean() {
		System.out.println("\n[fetchGarbageCollectionMXBean]");
		// GarbageCollectorMXBean 继承自 MemoryManagerMXBean
		List<GarbageCollectorMXBean> mxbeans = ManagementFactory.getGarbageCollectorMXBeans();
		System.out.println();
		for (GarbageCollectorMXBean mxbean : mxbeans) {
			System.out.println(String.format(">>> name = %s, count = %d, time = %d",
					mxbean.getName(), mxbean.getCollectionCount(), mxbean.getCollectionTime()));
		}
	}
	
	// 线程系统相关的信息（线程信息、守护线程、死锁线程）
	private static void fetchThreadMXBean() {
		System.out.println("\n[fetchThreadMXBean]");
		ThreadMXBean mxbean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] threadInfos = mxbean.dumpAllThreads(true, true);
		// ThreadInfo 包含了线程号、堆栈信息、阻塞信息、锁信息等
		for (ThreadInfo info : threadInfos) {
			System.out.println(">>> " + info.toString());
		}
	}
	
	// 当前操作系统相关的信息
	private static void fetchOperatingSystemMXBean() {
		System.out.println("\n[fetchOperatingSystemMXBean]");
		OperatingSystemMXBean mxbean = ManagementFactory.getOperatingSystemMXBean();
		System.out.printf("arch = %s, availableProcessors = %d, version = %s, name = %s, systemLoadAverage = %s\n",
				mxbean.getArch(), mxbean.getAvailableProcessors(), mxbean.getVersion(), 
				mxbean.getName(), mxbean.getSystemLoadAverage());
	}
	
	// 编译系统相关的信息
	private static void fetchCompilationMXBean() {
		System.out.println("\n[fetchCompilationMXBean]");
		CompilationMXBean mxbean = ManagementFactory.getCompilationMXBean();
		System.out.printf("name = %s, totalCompilationTime = %d\n", mxbean.getName(), mxbean.getTotalCompilationTime());
	}
	
	/** 缓冲池相关的信息
	 * 主要是堆外内存和MappedByteBuffer直接字节缓冲区
	 * >>> name = direct, count = 0, totalCapacity = 0, memoryUsed = 0
	 * >>> name = mapped, count = 0, totalCapacity = 0, memoryUsed = 0
	 */
	private static void fetchBufferPoolMXBean() {
		System.out.println("\n[fetchBufferPoolMXBean]");
		List<BufferPoolMXBean> mxbeans = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);
		for (BufferPoolMXBean mxbean : mxbeans) {
			System.out.printf(">>> name = %s, count = %d, totalCapacity = %d, memoryUsed = %d\n",
					mxbean.getName(), mxbean.getCount(), mxbean.getTotalCapacity(), mxbean.getMemoryUsed());
		}
	}
}
