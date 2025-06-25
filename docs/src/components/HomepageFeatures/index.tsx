import React, {ReactNode} from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

type FeatureItem = {
    title: string;
    Svg: React.ComponentType<React.ComponentProps<'svg'>>;
    description: ReactNode;
};

const FeatureList: FeatureItem[] = [
    {
        title: '极简开发：仅需实体类即可启动',
        Svg: require('@site/static/img/undraw_docusaurus_mountain.svg').default,
        description: (
            <>
                通过引入注解和依赖，系统会在编译期自动生成 Mapper 接口、 XML 等文件，大大减少样板代码，像使用 Lombok 一样。
            </>
        ),
    },
    {
        title: '保留 MyBatis-XML 优势：所见所得',
        Svg: require('@site/static/img/undraw_docusaurus_tree.svg').default,
        description: (
            <>
                完全基于 Mybatis XML ，几乎为零的学习成本 ， 保留可读性和结构化，使得 SQL 编写和结构体映射“所见即所得”，同时又避免了繁琐的手动维护。
            </>
        ),
    },
    {
        title: '多数据库支持与可扩展性强',
        Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
        description: (
            <>
                支持 oracle、mssql、postgresql、mysql、mariadb、dameng（达梦）语法生成、自动建表，并且支持多种风格 sql 语句，方便在不同环境中快速落地和迁移。
            </>
        ),
    },
];

function Feature({title, Svg, description}: FeatureItem) {
    return (
        <div className={clsx('col col--4')}>
            <div className="text--center">
                <Svg className={styles.featureSvg} role="img"/>
            </div>
            <div className="text--center padding-horiz--md">
                <Heading as="h3">{title}</Heading>
                <p>{description}</p>
            </div>
        </div>
    );
}

export default function HomepageFeatures(): ReactNode {
    return (
        <section className={styles.features}>
            <div className="container">
                <div className="row">
                    {FeatureList.map((props, idx) => (
                        <Feature key={idx} {...props} />
                    ))}
                </div>
            </div>
        </section>
    );
}
