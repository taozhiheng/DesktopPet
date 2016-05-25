package com.persist.desktoppet.bean;

/**
 * Created by taozhiheng on 16-5-25.
 *
 */
public class Reply {

    public final static int TYPE_USER = 0;
    public final static int TYPE_CLIENT = 1;

    public int type;
    public String content;

    public Reply(int type, String content)
    {
        this.type = type;
        this.content = content;
    }

}
