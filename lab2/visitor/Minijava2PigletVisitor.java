package visitor;
import symbol.*;
import syntaxtree.*;
import java.io.*;
import print.*;
import java.util.HashMap;

public class Minijava2PigletVisitor extends GJDepthFirst<MType, MType>
{
    private MClassList mClassList;  //存储class list
    private HashMap<String, String> tmpVar = new HashMap<String, String>();
    //记录所有临时变量，其中第一个String是class_method_name，第二个String是temp的编号
    private int tmpNumber = 20;
    //记录现在用到了TEMP 多少
    private int labelNumber = 0;
    //记录现在用到了L 多少

    private String getNumber(MMethod thisMethod, String name)
    {
        MClass thisClass = thisMethod.owner;
        String idx = thisClass.getName()+"_"+thisMethod.getName()+"_"+name;
        if (tmpVar.containsKey(idx))
        {
            return tmpVar.get(idx);
        }
        else
        {
            tmpVar.put(idx, ""+tmpNumber);
            tmpNumber ++;
            return ""+(tmpNumber-1);
        }
    }

    /**
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
    public MType visit(Goal n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    public MType visit(MainClass n, MType argu) {
        mClassList = (MClassList)argu;
        MType _ret=null;
        MIdentifier mIdentifier;
        PigletPrint.printMain();
        n.f0.accept(this, argu);
        mIdentifier = (MIdentifier)n.f1.accept(this, argu);
        MClass thisClass = mClassList.getClass(mIdentifier.getName());
        MMethod thisMethod = thisClass.getMethod("main");
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, thisMethod);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, thisMethod);
        n.f15.accept(this, thisMethod);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        PigletPrint.printEnd();
        return _ret;
    }

    /**
     * f0 -> ClassDeclaration()
     *       | ClassExtendsDeclaration()
     */
    public MType visit(TypeDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public MType visit(ClassDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        MIdentifier identifier = (MIdentifier)n.f1.accept(this, argu);
        MClass thisClass = ((MClassList)argu).getClass(identifier.getName());
        n.f2.accept(this, argu);
        n.f3.accept(this, thisClass);
        n.f4.accept(this, thisClass);
        n.f5.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    public MType visit(ClassExtendsDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        MIdentifier identifier = (MIdentifier)n.f1.accept(this, argu);
        MClass thisClass = ((MClassList)argu).getClass(identifier.getName());
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, thisClass);
        n.f6.accept(this, thisClass);
        n.f7.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public MType visit(VarDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    //这里argu是MClass
    public MType visit(MethodDeclaration n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String name = ((MIdentifier)n.f2.accept(this, argu)).getName();
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        MMethod thisMethod = ((MClass)argu).getMethod(name);
        int paraNum = thisMethod.getParaNum();
        PigletPrint.printMethod(((MClass)argu).getName(), name, paraNum+1);
        PigletPrint.printBegin();
        n.f7.accept(this, thisMethod);
        n.f8.accept(this, thisMethod);
        n.f9.accept(this, argu);
        PigletPrint.printReturn();
        n.f10.accept(this, thisMethod);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        PigletPrint.println("");
        PigletPrint.printEnd();
        return _ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public MType visit(FormalParameterList n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public MType visit(FormalParameter n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public MType visit(FormalParameterRest n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public MType visit(Type n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public MType visit(ArrayType n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "boolean"
     */
    public MType visit(BooleanType n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     */
    public MType visit(IntegerType n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Block()
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
     */
    public MType visit(Statement n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public MType visit(Block n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    //判断Identifier是一个临时变量还是一个类变量，当前argu是method
    public MType visit(AssignmentStatement n, MType argu) {
        MType _ret=null;
        MIdentifier identifier = (MIdentifier)n.f0.accept(this, argu);
        String name = identifier.getName();
        MMethod thisMethod = (MMethod)argu;
        MClass thisClass = thisMethod.owner;
        if (thisMethod.hasVar(name))    //是临时变量
        {
            String num = getNumber(thisMethod, name);
            PigletPrint.print("MOVE TEMP "+num+" ");
        }
        else    //是类变量，则从传进来的类实例表(TEMP 0)里获取相应的位置，默认int，boolean，array都是4个字节
        {
            PigletPrint.print("HSTORE TEMP 0 "+thisClass.getVarPos(mClassList, name)+" ");
        }
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        PigletPrint.println("");
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    public MType visit(ArrayAssignmentStatement n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    // CJUMP exp1 falseLabel
    // ...
    // JUMP statementNextLabel
    // falseLabel statement
    // statementNextLabel statement
    public MType visit(IfStatement n, MType argu) {
        MType _ret=null;
        int sNext = labelNumber++;
        int bFalse = labelNumber++;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        PigletPrint.print("CJUMP ");
        n.f2.accept(this, argu);
        PigletPrint.println("L"+bFalse);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        PigletPrint.println("JUMP L"+sNext);
        n.f5.accept(this, argu);
        PigletPrint.println("L"+bFalse+" NOOP"); //防止else里面没有statement
        n.f6.accept(this, argu);
        PigletPrint.println("L"+sNext+" NOOP"); //为了防止sNext后跟的是END等不符合规则的语句，加上NOOP
        return _ret;
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    // bStart CJUMP exp1 bFalse
    //  ...
    //  JUMP bStart
    // bFalse ...
    public MType visit(WhileStatement n, MType argu) {
        MType _ret=null;
        int bStart = labelNumber++;
        int bFalse = labelNumber++;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        PigletPrint.print("L"+bStart+" CJUMP ");
        n.f2.accept(this, argu);
        PigletPrint.println("L"+bFalse);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        PigletPrint.println("JUMP L"+bStart);
        PigletPrint.println("L"+bFalse+" NOOP");
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public MType visit(PrintStatement n, MType argu) {
        MType _ret=null;
        PigletPrint.print("PRINT ");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        PigletPrint.println("");
        return _ret;
    }

    /**
     * f0 -> AndExpression()
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
     */
    public MType visit(Expression n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public MType visit(AndExpression n, MType argu) {
        MType _ret=null;
        int tNum = tmpNumber++; //存放返回变量
        int lNum = labelNumber++;   //跳转的RETURN的label
        PigletPrint.printBegin();
        PigletPrint.println("MOVE TEMP "+tNum+" 0");
        PigletPrint.print("CJMP ");
        n.f0.accept(this, argu);
        PigletPrint.println("L"+lNum);  //若f0是0则直接return 0
        n.f1.accept(this, argu);
        PigletPrint.print("MOVE TEMP "+tNum+" ");
        n.f2.accept(this, argu);        //否则，f2是什么赋值给返回值什么
        PigletPrint.print("L"+lNum+" ");
        PigletPrint.printReturn();
        PigletPrint.println("TEMP "+tNum);
        PigletPrint.printEnd();
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    // LT exp1 exp2 就相当于 exp1 < exp2
    public MType visit(CompareExpression n, MType argu) {
        MType _ret=null;
        PigletPrint.print("LT ");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public MType visit(PlusExpression n, MType argu) {
        MType _ret=null;
        PigletPrint.print("PLUS ");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public MType visit(MinusExpression n, MType argu) {
        MType _ret=null;
        PigletPrint.print("MINUS ");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public MType visit(TimesExpression n, MType argu) {
        MType _ret=null;
        PigletPrint.print("TIMES ");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public MType visit(ArrayLookup n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public MType visit(ArrayLength n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    // A.a() 方法调用
    // f0 是 MClass
    public MType visit(MessageSend n, MType argu) {
        MType _ret=null;
        MIdentifier mIdentifier;
        MMethod thisMethod;
        MClass thisClass, whichClass=null;
        int vTableNum = tmpNumber++;
        int dTableNum = tmpNumber++;
        int tNum = tmpNumber++;
        PigletPrint.println("CALL");
        PigletPrint.printBegin();
        PigletPrint.print("MOVE TEMP "+vTableNum+" ");
        mIdentifier = (MIdentifier)n.f0.accept(this, argu); //这里传下去是identifier，如果是b = new A() b.a()的话，会将
                                                            // b的TEMP *输出
        PigletPrint.println("");

        thisMethod = (MMethod)argu;
        thisClass = mClassList.getClass(thisMethod.getVarType(mIdentifier.getName()).getType());

        mIdentifier = (MIdentifier)n.f2.accept(this, argu);

        PigletPrint.println("HLOAD TEMP "+tNum+" TEMP "+vTableNum+" "+thisClass.getMethodClassPos(mClassList,
                mIdentifier.getName(), whichClass));     //抽出对应的DTable
        PigletPrint.println("HLOAD TEMP "+dTableNum+" TEMP "+tNum+" "+whichClass.getMethodPos(mIdentifier.getName()));
        PigletPrint.printReturn();
        PigletPrint.println("TEMP "+dTableNum);
        PigletPrint.printEnd();
        PigletPrint.print("(TEMP "+vTableNum+" ");
        n.f4.accept(this, argu);
        PigletPrint.println(")");
        n.f5.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public MType visit(ExpressionList n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public MType visit(ExpressionRest n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
     */
    // 若f0是int true false NotExp，均会在更底层输出
    // 若是identifier，考虑到变量定义的时候不应该输出，则不会输出，会返回一个MIdentifier，在这一层输出
    // 其他的应该也不会返回吧...
    public MType visit(PrimaryExpression n, MType argu) {
        MType _ret=null;
        MType f0 = n.f0.accept(this, argu);
        if (f0 instanceof MIdentifier) // 是identifier的情况
        {
            MIdentifier identifier = (MIdentifier)f0;
            String name = identifier.getName();
            MMethod thisMethod = (MMethod)argu;
            MClass thisClass = thisMethod.owner;
            if (thisMethod.hasVar(name))    //是临时变量
            {
                String num = getNumber(thisMethod, name);
                PigletPrint.print("TEMP "+num);
            }
            else    //是类变量，则从传进来的类实例表(TEMP 0)里获取相应的位置，默认int，boolean，array都是4个字节
            {
                int tNum = tmpNumber++;
                PigletPrint.printBegin();
                PigletPrint.println("HLOAD TEMP "+tNum+" TEMP 0 "+thisClass.getVarPos(mClassList, name));
                PigletPrint.printReturn();
                PigletPrint.print("TEMP "+tNum);
                PigletPrint.printEnd();
            }
            return f0;
        }
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public MType visit(IntegerLiteral n, MType argu) {
        MType _ret=null;
        PigletPrint.print(n.f0.toString()+" ");
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "true"
     */
    public MType visit(TrueLiteral n, MType argu) {
        MType _ret=null;
        PigletPrint.print("1 ");
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "false"
     */
    public MType visit(FalseLiteral n, MType argu) {
        MType _ret=null;
        PigletPrint.print("0 ");
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public MType visit(Identifier n, MType argu) {
        MType _ret=null;
        String name = n.f0.toString();
        _ret = new MIdentifier(-1, -1, name, name);
        return _ret;
    }

    /**
     * f0 -> "this"
     */
    public MType visit(ThisExpression n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public MType visit(ArrayAllocationExpression n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    // 构造类实例表，注意DTable前，以及注意父类表的接续(todo)
    public MType visit(AllocationExpression n, MType argu) {
        MType _ret=null;
        MIdentifier mIdentifier;
        MClass thisClass;
        n.f0.accept(this, argu);
        mIdentifier = (MIdentifier)n.f1.accept(this, argu);
        thisClass = mClassList.getClass(mIdentifier.getName());
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);

        int dTableNum = tmpNumber++;
        int vTableNum = tmpNumber++;
        PigletPrint.printBegin();
        // 分配DTable
        PigletPrint.println("MOVE TEMP "+dTableNum+" HALLOCATE "+(4*thisClass.mMethodVec.size()));
        for (int i = 0; i < thisClass.mMethodVec.size(); ++i)
        {
            PigletPrint.println("HSTORE TEMP "+dTableNum+" "+4*i+" "+thisClass.getName()+"_"+
                    thisClass.getMethod(i).getName());
        }
        // 分配VTable
        PigletPrint.println("MOVE TEMP "+vTableNum+" HALLOCATE "+(4*(thisClass.mVarVec.size()+1)));
        PigletPrint.println("HSTORE TEMP "+vTableNum+" 0 "+"TEMP "+dTableNum);   //存放dTable地址
        for (int i = 0; i < thisClass.mVarVec.size(); ++i)
        {
            PigletPrint.println("HSTORE TEMP "+vTableNum+" "+4*(i+1)+" 0"); // 为类变量全部初始化为0
        }
        PigletPrint.printReturn();
        PigletPrint.println("TEMP "+vTableNum);
        PigletPrint.printEnd();
        return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    // exp1 = 0 or 1
    // !exp1 = 1 or 0
    // exp1+!exp1=1
    public MType visit(NotExpression n, MType argu) {
        MType _ret=null;
        PigletPrint.print("MINUS 1 ");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public MType visit(BracketExpression n, MType argu) {
        MType _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }
}