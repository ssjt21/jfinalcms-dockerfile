package cms;

import java.util.List;

import com.cms.entity.Category;
import com.cms.entity.Content;

/**
 * @description : 
 * @author : heyewei
 * @create : 2020年12月2日
 **/
public class ContentTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "select * from cms_content where categoryId in ( select id from cms_category where  modelId=4)";
		List<Content> contentList = new Content().dao().find(sql);
		for(Content content : contentList){
			Category category = content.getCategory();
			category.setIntroduction(content.getIntroduction());
			category.setImage(content.getIco());
			category.update();
		}
	}

}
