package drools.issues.model.vehicles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Printer;

public class ASMDissassembleTest {

	@Test
	public void test() throws IOException {
		GasolineModelGenerator generator = new GasolineModelGenerator(getClass().getClassLoader());
		Class<? extends Vehicle> gasolineVehicleClass = generator.getVehicleClass();
		Class<? extends Engine> engineClass = generator.getEngineClass();
		Class<? extends Vehicle> dieselVehicleClass = DieselCar.class;
		assertEquals(dieselVehicleClass.getSuperclass(), gasolineVehicleClass.getSuperclass());
		ClassReader gasolineVehicleReader = new ClassReader(generator.getClassLoader()
				.getResourceAsStream(gasolineVehicleClass.getName().replace('.', '/') + ".class"));
		ClassReader dieselVehicleReader = new ClassReader(generator.getClassLoader()
				.getResourceAsStream(dieselVehicleClass.getName().replace('.', '/') + ".class"));
		gasolineVehicleReader.accept(new CustomClassVisitor(), 0);
		dieselVehicleReader.accept(new CustomClassVisitor(), 0);
		writeClass(gasolineVehicleClass, generator.getClassLoader());
		writeClass(engineClass, generator.getClassLoader());
		writeClass(DieselCar.class, generator.getClassLoader());
		writeClass(DieselEngine.class, generator.getClassLoader());
	}

	private void writeClass(Class<?> clazz, ClassLoader classLoader) throws IOException {
		try (var data = classLoader.getResourceAsStream(clazz.getName().replace('.', '/') + ".class")) {
			FileUtils.copyInputStreamToFile(data, new File("/tmp/" + clazz.getSimpleName() + ".class"));
		}

	}

	class CustomClassVisitor extends ClassVisitor {

		public CustomClassVisitor() {
			super(Opcodes.ASM9);
		}

		@Override
		public void visit(int version, int access, String name, String signature, String superName,
				String[] interfaces) {
			System.out.println("Class: " + name);
			System.out.println("Superclass: " + superName);
			System.out.println("Implements: " + Arrays.toString(interfaces));
			super.visit(version, access, name, signature, superName, interfaces);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
				String[] exceptions) {
			System.out.println("\nMethod: " + name + descriptor);
			return new CustomMethodVisitor();
		}

		@Override
		public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
			System.out.println("Field: " + name + " " + descriptor);
			if (signature != null) {
				System.out.println("Signature: " + signature);
			}
			if (value != null) {
				System.out.println("Value: " + value);
			}
			return super.visitField(access, name, descriptor, signature, value);
		}
	}

	class CustomMethodVisitor extends MethodVisitor {
		public CustomMethodVisitor() {
			super(Opcodes.ASM9);
		}

		@Override
		public void visitInsn(int opcode) {
			System.out.println("Instruction: " + Printer.OPCODES[opcode]);
			super.visitInsn(opcode);
		}

		@Override
		public void visitVarInsn(int opcode, int var) {
			System.out.println("Variable Instruction: " + Printer.OPCODES[opcode] + " " + var);
			super.visitVarInsn(opcode, var);
		}
	}
}