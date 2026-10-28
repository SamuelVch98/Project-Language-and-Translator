package compiler.Code_generation;

public interface CodeGenerationVisitable {
    void acceptCodeGeneration(CodeGenerationVisitor visitor);
}
