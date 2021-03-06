package bytecode;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;

/**
 * @author Vea
 * @since 2019-02
 */
public final class OkHttpClassAdapter extends ClassVisitor {
    private String className;
    private boolean weaveEventListener;

    OkHttpClassAdapter(final ClassVisitor cv, boolean weaveEventListener) {
        super(Opcodes.ASM5, cv);
        this.weaveEventListener = weaveEventListener;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
        final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (className.equals("okhttp3/OkHttpClient$Builder")) {
            return mv == null ? null : new OkHttpMethodAdapter(className + File.separator + name, access, desc, mv, weaveEventListener);
        } else {
            return mv;
        }
    }
}