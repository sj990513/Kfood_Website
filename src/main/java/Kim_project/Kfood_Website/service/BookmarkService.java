package Kim_project.Kfood_Website.service;

import Kim_project.Kfood_Website.domain.Bookmark;
import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.repository.Bookmark.BookmarkRepository;
import Kim_project.Kfood_Website.repository.Menu.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository, MenuRepository menuRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.menuRepository = menuRepository;
    }

    //북마크추가
    public void addBookmark(Member member, Menu menu) {
        bookmarkRepository.addBookmark(member, menu);
    }

    //북마크 정렬
    public List<Bookmark> sortBookmark(Member member) {
        return  bookmarkRepository.FindAllOfMember(member);
    }

    //중복확인
    public boolean validateMenu(Member member, Menu menu) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByMemberAndMenu(member, menu);

        //중복아닐시
        if(bookmark.isEmpty())
            return true;

        //중복
        else
            return false;
    }

    //북마크삭제
    public void deleteBookmark(Member member, Long menuNumber) {
        bookmarkRepository.deleteBookmark(member.getMemberId(), menuNumber);
    }

    //검색어로 검색
    public List<Menu> sortByBookmarkAndSearch(String search, Member member) {


        //우선 북마크들을 불러온다
        List<Bookmark> bookmarks = bookmarkRepository.FindAllOfMember(member);

        List<Menu> menus = new ArrayList<>();

        //북마크 하나씩 대조를 진행한다.
        for (int i=0; i< bookmarks.size(); i++) {
            //findByMenuNameAndMenuNumber는 리스트로 불러오기때문에 리스트안에 값이있으면 ( 검색어에 해당하는 메뉴리스트가 존재하면 )
            if( menuRepository.findByMenuNameAndMenuNumber( search, bookmarks.get(i).getMenuNumber()) != null &&
                    !menuRepository.findByMenuNameAndMenuNumber( search, bookmarks.get(i).getMenuNumber()).isEmpty() )

                //menus에 추가해준다. (리스트당 하나밖에 존재하지않으니 0번쨰 인덱스를 저장)
                menus.add(menuRepository.findByMenuNameAndMenuNumber( search, bookmarks.get(i).getMenuNumber()).get(0));
        }

        return  menus;
    }
}
