import { loadUserCookie } from "@/util/util";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Header() {
  const [login, setLogin] = useState(false);

  useEffect(() => {
    const user = loadUserCookie();
    setLogin(user != null);
  }, []);

  return (
    <>
      <header className="header">
        <div className="header-content">
          <h1>
            <Link href={"/"}>Yeah-Board</Link>
          </h1>
          <div className="header-link">
            {!login ? (
              <>
                <Link href="/signup">회원가입</Link>
                <Link href="/login">로그인</Link>
              </>
            ) : (
              <Link href="/write">글쓰기</Link>
            )}
          </div>
        </div>
      </header>
    </>
  );
}
