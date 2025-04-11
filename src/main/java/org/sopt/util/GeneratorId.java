package org.sopt.util;

// Id를 생성하는 유틸 클래스
public final class GeneratorId {
    // public final + private 조합 -> 상속 불가, 인스턴스 생성 불가
    private GeneratorId(){}

    private static int id = 1;

    public static int generateId(){
        return id++;
    }

}
