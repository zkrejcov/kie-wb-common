package test;

public class Outer
{

    public enum PublicInnerEnum {
        E1, E2
    }

    public static enum PublicStaticInnerEnum {
        E1, E2
    }

    private enum PrivateInnerEnum {
        E1, E2
    }

    private static enum PrivateStaticInnerEnum {
        E1, E2
    }


    public class PublicInnerClass {
        String innerField1;

        public class PublicInnerClassInner {

        }

        public PublicInnerClass()
        {
        }

        public PublicInnerClass(String innerField1) {
            this.innerField1 = innerField1;
        }
    }

    public static class PublicStaticInnerClass {
        String innerField1;

        public PublicStaticInnerClass()
        {
        }

        public PublicStaticInnerClass(String innerField1) {
            this.innerField1 = innerField1;
        }
    }

    private class PrivateInnerClass {
        String innerField1;

        public PrivateInnerClass()
        {
        }

        public PrivateInnerClass(String innerField1) {
            this.innerField1 = innerField1;
        }
    }

    private static class PrivateStaticInnerClass {
        String innerField1;

        public PrivateStaticInnerClass()
        {
        }

        public PrivateStaticInnerClass(String innerField1) {
            this.innerField1 = innerField1;
        }
    }


    //enum fields usages
    Outer.PublicInnerEnum publicInnerEnumQualified;

    PublicInnerEnum publicInnerEnum;

    PublicStaticInnerEnum publicStaticInnerEnum;

    Outer.PublicStaticInnerEnum publicStaticInnerEnumQualified;

    PrivateInnerEnum privateInnerEnum;

    Outer.PrivateInnerEnum privateInnerEnumQualified;

    PrivateStaticInnerEnum privateStaticInnerEnum;

    Outer.PrivateStaticInnerEnum privateStaticInnerEnumQualified;

    //inner class usages
    Outer.PublicInnerClass publicInnerClassQualified;

    PublicInnerClass publicInnerClass;

    Outer.PublicStaticInnerClass publicStaticInnerClassQualified;

    PublicStaticInnerClass publicStaticInnerClass;

    Outer.PrivateInnerClass privateInnerClassQualified;

    PrivateInnerClass privateInnerClass;

    Outer.PrivateStaticInnerClass privateStaticInnerClassQualified;

    PrivateStaticInnerClass privateStaticInnerClass;

    public Outer() { }

}
