/**
 * File: common/utils/touchEffect.ts
 * Description:
 *   버튼 클릭 시 눌림 효과를 제공한다
 */

export const touchEffect = {
    onTouchStart: (e: React.TouchEvent<HTMLButtonElement>) => {
        e.currentTarget.style.transform = "scale(0.97)";
        e.currentTarget.style.transition = "transform 0.1s ease";
    },
    onTouchEnd: (e: React.TouchEvent<HTMLButtonElement>) => {
        e.currentTarget.style.transform = "scale(1)";
    },
    onMouseDown: (e: React.MouseEvent<HTMLButtonElement>) => {
        e.currentTarget.style.transform = "scale(0.97)";
        e.currentTarget.style.transition = "transform 0.1s ease";
    },
    onMouseUp: (e: React.MouseEvent<HTMLButtonElement>) => {
        e.currentTarget.style.transform = "scale(1)";
    },
};