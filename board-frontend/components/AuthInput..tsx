import { NextPage } from "next";

interface Props {
  value: string;
  setValue: (value: string) => void;
  type: string;
  placeholder: string;
}

const AuthInput: NextPage<Props> = ({ value, setValue, type, placeholder }) => {
  return (
    <input
      className="auth-input"
      type={type}
      value={value}
      onChange={(e) => {
        setValue(e.target.value);
      }}
      placeholder={placeholder}
    />
  );
};

export default AuthInput;
