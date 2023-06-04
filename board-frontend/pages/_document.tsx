import Header from "@/components/Header";
import { Head, Html, Main, NextScript } from "next/document";

export default function Document() {
  return (
    <Html lang="ko">
      <Head />
      <body>
        <Header />
        <main>
          <Main />
        </main>
        <NextScript />
      </body>
    </Html>
  );
}
