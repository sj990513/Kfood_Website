package Kim_project.Kfood_Website.repository.Bookmark;

import Kim_project.Kfood_Website.domain.Bookmark;
import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.domain.Menu;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository {

    //북마크 추가
    void addBookmark(Member member, Menu menu);

    //특정멤버 북마크 전체정렬
    List<Bookmark> FindAllOfMember(Member member);

    //멤버이름으로 북마크찾기
    Optional<Bookmark> findByMemberId(Member member);

    //메뉴넘버로 북마크찾기
    Optional<Bookmark> findByMenuNumber(Menu menu);

    //특정멤버의 메뉴넘버으로 북마크찾기
    Optional<Bookmark> findByMemberAndMenu(Member member, Menu menu);

    //특정멤버의 북마크삭제
    void deleteBookmark(String memberId, Long menuNumber);

}
