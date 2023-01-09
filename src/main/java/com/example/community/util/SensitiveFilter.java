package com.example.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.tree.TreeNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    private static final String  REPLACEMENT = "***";   //替换符
    private TrieNode root = new TrieNode(); //根节点

    public SensitiveFilter(){
        this.init();
    }

    public void init(){     //构造器方法执行后此方法将自动调用
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");//编译后的class路径下的文件
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        )
        {
            String keyword;
            while((keyword = reader.readLine()) != null) {
                this.addKeyWord(keyword);//添加到前缀树
            }
        } catch (IOException e){
            logger.error("加载敏感词文件失败："+e.getMessage());
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤文本
     * @return  已过滤文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)) return "";
        StringBuilder sb = new StringBuilder();
        for(int st = 0;st<text.length();st++)
        {
            if(this.root.sonNodes.containsKey(text.charAt(st)))
            {
                boolean testAble = true;
                for(int en = st;en<text.length()&&testAble;en++)
                {
                    TrieNode node = this.root;
                    int index;
                    for(index=st;index<=en;index++)
                    {
                        if(isSymbol(text.charAt(index))) continue;
                        if(node.sonNodes.containsKey(text.charAt(index))){
                            node = node.sonNodes.get(text.charAt(index));
                        } else {
                            testAble = false;
                            break;
                        }
                    }
                    if(index > en && node.isKeyWordEnd)
                    {
                        sb.append(REPLACEMENT);
                        st=en;
                        break;
                    }
                }
                if(testAble == false){
                    sb.append(text.charAt(st));
                }
            } else {
                sb.append(text.charAt(st));
            }
        }
        return sb.toString();
    }

    public boolean isSymbol(Character c){
        // 不是英文字母，不是数字，且不在东亚文字范围内，一般就是各种无实际意义的符号
        return !CharUtils.isAsciiAlphanumeric(c) && (c>0x9FFF || c<0x2E80);
    }
    private void addKeyWord(String keyword)//将一个敏感词添加到前缀树
    {
        TrieNode node = this.root;
        int i;
        for(i=0;i<keyword.length();i++)
        {
            if(node.sonNodes.containsKey(keyword.charAt(i))){//当前节点仍然在树中存在
                node = node.sonNodes.get(keyword.charAt(i));
            } else break;
        }
        //上面没处理完,也就是存在部分字母不在树上
        //把剩下的字母串在node节点下面
        while(i<keyword.length()){
            TrieNode n = new TrieNode();
            node.sonNodes.put(keyword.charAt(i),n);
            node = n;
            i++;
        }
        node.isKeyWordEnd = true;
    }

    private class TrieNode{
        boolean isKeyWordEnd = false;
        Map<Character,TrieNode> sonNodes = new HashMap<>();
    }


}
