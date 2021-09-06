package serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class DoMain {

	/**
	 * @param args
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		deserialize();
//		serialize();
	}

	static void serialize() {
		try {
			String file = "obj.bin";
			ComplexXtension complex = new ComplexXtension();
			complex.setId(1111);
			complex.setName("abc");
			complex.setType("t1");
			complex.setMap(new HashMap<String, String>() {{
				put("key1", "value1");
				put("key2", "value2");
			}});
			complex.setValue(new ArrayList<String>() {{
				add("xxx11");
				add("xxx22");
			}});
			
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(complex);
			oos.flush();
			
			String hfile = "obj.hessian.bin";
			FileOutputStream fosh = new FileOutputStream(hfile);
			Hessian2Output houtput = new Hessian2Output(fosh);
			houtput.writeObject(complex);
			houtput.flush();
			fosh.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	static void deserialize() {
		try {
			String hfile = "obj.hessian.bin";
			FileInputStream fish = new FileInputStream(hfile);
			Hessian2Input hinput = new Hessian2Input(fish);
			ComplexXtension complex = (ComplexXtension)hinput.readObject();
			System.out.println(complex.getName());
			System.out.println(complex.getId());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
