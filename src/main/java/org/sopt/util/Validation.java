package org.sopt.util;

// final 키워드 사용하여 상속 방지
public final class Validation {

    // java:S1118
    // private를 사용하여 인스턴스 생성 방지
    private Validation(){
        throw new IllegalArgumentException("Utility Class");
    }

    public static void isTitleValid(String title) {
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ 제목을 입력해야 합니다.");
        }
        if (title.length() > 30){
            throw new IllegalArgumentException("❌ 제목은 30자 이하여야 합니다.");
        }
    }

}
