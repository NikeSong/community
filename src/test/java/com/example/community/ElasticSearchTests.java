package com.example.community;

import com.example.community.dao.DiscussPostMapper;
import com.example.community.dao.elasticsearch.DiscussPostRepository;
import com.example.community.entity.DiscussPost;
import org.apache.commons.codec.binary.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class ElasticSearchTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;//操作elasticsearch的工具类

    @Test
    public void testInsert()
    {
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(241));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(242));
        discussPostRepository.save(discussPostMapper.selectDiscussPostById(243));
    }

    @Test
    public void testInsertList() {
        for(int i=101;i<=200;i++)
        {
            List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPost(i, 0, 100);
            if(discussPosts != null){
                for(DiscussPost post:discussPosts)
                {
                    discussPostRepository.save(post);
                }

            }
        }
    }

    @Test
    public void testUpdate()
    {
        DiscussPost post = discussPostMapper.selectDiscussPostById(231);
        post.setContent("just like this, when you get a work");
        discussPostRepository.save(post);
    }

    @Test
    public void testDelete()
    {
        discussPostRepository.deleteById(231);
    }

    @Test
    public void testDeleteAll()
    {
        discussPostRepository.deleteAll();
    }

    @Test
    public void testSearchByRepository()
    {
        /**
         * 搜索词为互联网寒冬，在标题和内容中找
         * 结果按照帖子类型倒序排列，帖子分数倒叙排列，事件倒序排列
         * 当前查询前10条
         * 返回的数据中，符合搜索词的用em标签标注
         */
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))    //从第0页开始显示，一共显示10条
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        SearchHits<DiscussPost> search1 = elasticsearchTemplate.search(searchQuery, DiscussPost.class);

        System.out.println("");
        System.out.println(search1.getTotalHits());
        for(SearchHit<DiscussPost> hint:search1) {
            DiscussPost discussPost = hint.getContent();
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
                    discussPost.setTitle(sb.toString());
                    System.out.println(discussPost);
                }
            }
        }

    }
}
