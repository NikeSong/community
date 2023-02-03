package com.example.community.controller;

import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.service.ElasticSearchService;
import com.example.community.service.LikeService;
import com.example.community.service.UserService;
import com.example.community.util.CommunityConstant;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    //search?keyword=xxx
    @RequestMapping(path="/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) {
        // 搜索帖子
        SearchHits<DiscussPost> searchResult =
                elasticSearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        // 聚合数据
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if(searchResult != null){
            for(SearchHit<DiscussPost> hint:searchResult) {

                DiscussPost post = hint.getContent();
                getHighLightContent(hint,post);
                Map<String,Object> map  = new HashMap<>();
                // 帖子存进去
                map.put("post",post);
                // 帖子的作者
                map.put("user",userService.findUserById(post.getUserId()));
                // 点赞数量
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("keyword",keyword);
        // 分页信息
        page.setPath("/search?keyword="+keyword);
        page.setRows(searchResult == null ? 0:(int) searchResult.getTotalHits());

        return "/site/search";
    }

    private void getHighLightContent(SearchHit<DiscussPost> hint,DiscussPost discussPost){
        // 处理高亮
        Map<String, List<String>> highlightFields = hint.getHighlightFields();
        for (Map.Entry<String, List<String>> stringHighlightFieldEntry : highlightFields.entrySet()) {
            String key = stringHighlightFieldEntry.getKey();
            if (StringUtils.equals(key, "title")) {
                List<String> fragments = stringHighlightFieldEntry.getValue();
                StringBuilder sb = new StringBuilder();
                for (String fragment : fragments) {
                    sb.append(fragment);
                }
                discussPost.setTitle(sb.toString());
            }
        }
        for (Map.Entry<String, List<String>> stringHighlightFieldEntry : highlightFields.entrySet()) {
            String key = stringHighlightFieldEntry.getKey();
            if (StringUtils.equals(key, "content")) {
                List<String> fragments = stringHighlightFieldEntry.getValue();
                StringBuilder sb = new StringBuilder();
                for (String fragment : fragments) {
                    sb.append(fragment);
                }
                discussPost.setContent(sb.toString());
            }
        }
    }
}
