package agentmainsample;

import java.util.List;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
/**
 * 中途执行，加载agent
 * @author wangqiang
 */
public class AgentAttachMain {

	public static void main(String[] args) throws Exception {
		loadAgent("/Users/wangqiang/Programing/Web_Data/lab-agent-main.jar");
	}
	
	private static void loadAgent(String agentJarFile) throws Exception {
		List<VirtualMachineDescriptor> vmdlist = VirtualMachine.list();
		for (VirtualMachineDescriptor vmd : vmdlist) {
			if (vmd.displayName().endsWith("SampleOne")) {
				VirtualMachine vm = VirtualMachine.attach(vmd);
				vm.loadAgent(agentJarFile);
				System.out.println("loadAgent OK");
				vm.detach();
			}
		}
	}
}
